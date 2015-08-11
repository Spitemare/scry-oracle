package io.github.scry;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.scry.ScryOracleApplication.OracleProperties;
import io.github.scry.model.MtgCard;
import io.github.scry.model.MtgSet;
import io.github.scry.model.Printing;
import io.github.scry.sort.SetCardSorter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("!test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class OracleCreator implements ApplicationListener<ContextRefreshedEvent> {

    RestTemplate restTemplate;

    ObjectMapper objectMapper;

    OracleProperties oracleProperties;

    SetCardSorter setCardSorter;

    @Override
    @SneakyThrows(IOException.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Creating new Oracle DB", oracleProperties.getDownloadUrl());
        Path zip = download(oracleProperties.getDownloadUrl());
        Path json = unzip(zip);

        Collection<MtgCard> cards = unmarshal(json);

        LOG.info("Writing new DB to {}", oracleProperties.getFile());
        objectMapper.writer().writeValue(oracleProperties.getFile(), cards);

        Files.deleteIfExists(json);
        Files.deleteIfExists(zip);
    }

    private Path download(URI uri) throws IOException {
        LOG.info("Downloading {}", uri);

        if ("file".equals(uri.getScheme())) return Paths.get(uri);

        byte[] zip = restTemplate.getForObject(uri, byte[].class);
        LOG.info("Downloaded {}", uri);
        Path temp = Files.createTempFile("oracle", ".zip");
        try (InputStream input = new BufferedInputStream(new ByteArrayInputStream(zip))) {
            Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
        }
        return temp;
    }

    private Path unzip(Path zip) throws IOException {
        LOG.info("Unzipping {}", zip);
        try (ZipFile zipFile = new ZipFile(zip.toFile())) {
            // We know there is one entry in the zip file
            ZipEntry entry = zipFile.entries().nextElement();
            Path json = zip.getParent().resolve(entry.getName());
            try (InputStream input = zipFile.getInputStream(entry)) {
                Files.copy(input, json, StandardCopyOption.REPLACE_EXISTING);
            }
            LOG.info("Unzipped {} to {}", zip, json);
            return json;
        }
    }

    private Collection<MtgCard> unmarshal(Path json) throws IOException {
        Map<String, MtgCard> map = new HashMap<>();
        try (InputStream input = Files.newInputStream(json)) {
            JsonParser parser = objectMapper.getFactory().createParser(input);

            parser.nextValue(); // advance into top level object
            while (parser.nextValue() != null) {
                MtgSet set = parser.readValueAs(MtgSet.class);
                if (set == null) continue;

                LOG.info("Unmarshalled {}", set.getName());
                List<MtgCard> cards = setCardSorter.sortCardsIn(set);

                for (MtgCard c : cards) {
                    MtgCard card = map.getOrDefault(c.getName(), c);
                    card.getMultiverseids().addFirst(c.getMultiverseid());
                    card.getPrintings().addFirst(createPrinting(c, set));
                    map.put(card.getName(), card);
                }
            }
        }

        Collection<MtgCard> cards = map.values();
        for (MtgCard card : cards) {
            card.setArtist(null);
            card.setBorder(null);
            card.setFlavor(null);
            card.setMultiverseid(null);
            card.setNumber(null);
            card.setRarity(null);
            card.setColors(null);
        }

        return cards;
    }

    private Printing createPrinting(MtgCard card, MtgSet set) {
        Printing printing = new Printing();
        printing.setArtist(card.getArtist());
        printing.setBorder(card.getBorder() != null ? card.getBorder() : set.getBorder());
        printing.setFlavor(card.getFlavor());
        printing.setMci(set.getMciCode() != null ? set.getMciCode() : set.getCode().toLowerCase());
        printing.setMultiverseid(card.getMultiverseid());
        printing.setNumber(card.getNumber());
        printing.setRarity(card.getRarity());
        printing.setSetcode(set.getCode());
        printing.setSetname(set.getName());
        return printing;
    }

}
