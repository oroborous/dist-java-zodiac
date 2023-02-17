package edu.wctc.distjavazodiac.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wctc.distjavazodiac.entity.Birthday;
import edu.wctc.distjavazodiac.entity.Fortune;
import edu.wctc.distjavazodiac.entity.Horoscope;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
Random horoscopes from https://cafeastrology.com/dailyhoroscopesall-tomorrow.html
 */
@Service
public class RandomHoroscopeService implements HoroscopeService {
    private List<Fortune> allFortunes;
    private ZodiacService zodiacService;

    @Autowired
    public RandomHoroscopeService(ZodiacService zodiacService) {
        this.zodiacService = zodiacService;
    }

    @Override
    public Horoscope getHoroscope(Birthday birthday) {
        String sign;
        if (birthday.getZodiacType().toLowerCase().startsWith("w")) {
            sign = zodiacService.getWesternZodiacSign(birthday);
        } else {
            sign = zodiacService.getEasternZodiacSign(birthday);
        }

        Horoscope hscope = new Horoscope();
        hscope.setSign(sign);

        int randomIndex = (int) (Math.random() * allFortunes.size());
        hscope.setHoroscope(allFortunes.get(randomIndex).getText());
        return hscope;
    }

    @PostConstruct
    public void initHoroscopes() {
        ObjectMapper mapper = new ObjectMapper();

        Fortune[] fortuneArray;
        try {
            /*
             * To read a file that has been packaged/deployed with an application,
             * place the file under the 'resources' folder in a subdirectory structure
             * identical to the class that will read it.
             */
            fortuneArray = mapper.readValue(
                    RandomHoroscopeService.class.getResourceAsStream("fortunes.json"),
                    Fortune[].class);
        } catch (IOException e) {
            e.printStackTrace();
            fortuneArray = new Fortune[]{new Fortune()};
        }
        allFortunes = Arrays.asList(fortuneArray);
    }
}
