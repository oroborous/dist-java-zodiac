package edu.wctc.distjavazodiac.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wctc.distjavazodiac.entity.Birthday;
import edu.wctc.distjavazodiac.entity.Fortune;
import edu.wctc.distjavazodiac.entity.Horoscope;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
Random horoscopes from https://cafeastrology.com/dailyhoroscopesall-tomorrow.html
 */
@Service
public class RandomHoroscopeService implements HoroscopeService {
    @Value("classpath:fortunes.json")
    private Resource fortunes;
    private List<Fortune> allFortunes;
    private final ZodiacService zodiacService;

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
            fortuneArray = mapper.readValue(
                    fortunes.getFile(),
                    Fortune[].class);
        } catch (IOException e) {
            e.printStackTrace();
            fortuneArray = new Fortune[]{new Fortune()};
        }
        allFortunes = Arrays.asList(fortuneArray);
    }
}
