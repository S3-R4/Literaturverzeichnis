package test.testfolder;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.KI_Tool;

public class TestKI_Tool {

        @Test
        public void guteQuelleLang() {
                ArrayList<String> autor = new ArrayList<String>(List.of("OpenAI"));
                LocalDate veroeffentlichungsdatum = LocalDate.of(2023, 1, 1);
                String modellName = "ChatGPT";
                String modellVersion = "Mar 14";
                String toolArt = "Large Language Model";
                String url = "https://chat.openai.com/chat";
                LocalDate aufrufdatum = LocalDate.of(2025, 12, 5);
                KI_Tool ki = new KI_Tool(autor, veroeffentlichungsdatum, modellName, modellVersion, toolArt,
                                url, aufrufdatum);

                assertEquals(
                                "OpenAI. (2023). ChatGPT (Mar 14 version) [Large Language Model]. https://chat.openai.com/chat (05.12.2025).",
                                ki.generiereLangbeleg());
        }

        @Test
        public void guteQuelleKurz() {
                ArrayList<String> autor = new ArrayList<String>(List.of("OpenAI"));
                LocalDate veroeffentlichungsdatum = LocalDate.of(2023, 1, 1);
                String modellName = "ChatGPT";
                String modellVersion = "Mar 14";
                String toolArt = "Large Language Model";
                String url = "https://chat.openai.com/chat";
                LocalDate aufrufdatum = LocalDate.of(2025, 12, 5);
                KI_Tool ki = new KI_Tool(autor, veroeffentlichungsdatum, modellName, modellVersion, toolArt,
                                url, aufrufdatum);

                assertEquals(
                                "OpenAI. (2023). ChatGPT (Mar 14 version) [Large Language Model]. https://chat.openai.com/chat (05.12.2025).",
                                ki.generiereLangbeleg());
        }


}
