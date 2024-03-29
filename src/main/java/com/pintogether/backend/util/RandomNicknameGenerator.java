package com.pintogether.backend.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public final class RandomNicknameGenerator {

    static List<String> adjective;
    static List<String> adjectives2;
    static List<String> animalNames;

    public static String generateNickname() {
        List<String> adjectives = new ArrayList<>();
        List<String> adjectives2 = new ArrayList<>();

        adjectives.add("아름다운");
        adjectives.add("행복한");
        adjectives.add("멋진");
        adjectives.add("즐거운");
        adjectives.add("따뜻한");
        adjectives.add("귀여운");
        adjectives.add("밝은");
        adjectives.add("친절한");
        adjectives.add("신나는");
        adjectives.add("신비로운");
        adjectives.add("고운");
        adjectives.add("좋은");
        adjectives.add("착한");
        adjectives.add("설레는");
        adjectives.add("유쾌한");
        adjectives.add("예쁜");
        adjectives.add("편안한");
        adjectives.add("따스한");
        adjectives.add("정갈한");
        adjectives.add("화려한");
        adjectives.add("낭만적인");
        adjectives.add("재미있는");
        adjectives.add("귀중한");
        adjectives.add("신선한");
        adjectives.add("달콤한");
        adjectives.add("활기찬");
        adjectives.add("단순한");
        adjectives.add("조용한");
        adjectives.add("고상한");
        adjectives.add("화목한");
        adjectives.add("단호한");
        adjectives.add("독특한");
        adjectives.add("놀라운");
        adjectives.add("맑은");
        adjectives.add("웃긴");
        adjectives.add("건강한");
        adjectives.add("긍정적인");
        adjectives.add("고귀한");
        adjectives.add("꾸밈없는");
        adjectives.add("명랑한");
        adjectives.add("아늑한");
        adjectives.add("자유로운");
        adjectives.add("우아한");
        adjectives.add("아쉬운");
        adjectives.add("훌륭한");
        adjectives.add("날씬한");
        adjectives.add("바쁜");
        adjectives.add("훈훈한");
        adjectives.add("부드러운");
        adjectives.add("새로운");
        adjectives.add("유연한");
        adjectives.add("씩씩한");
        adjectives.add("대담한");
        adjectives.add("깨끗한");
        adjectives.add("명확한");
        adjectives.add("선명한");
        adjectives.add("성실한");
        adjectives.add("활발한");
        adjectives.add("호기심 많은");
        adjectives.add("용감한");
        adjectives.add("진실한");
        adjectives.add("단호한");
        adjectives.add("창의적인");
        adjectives.add("지혜로운");
        adjectives.add("침착한");
        adjectives.add("꿈같은");
        adjectives.add("자랑스러운");
        adjectives.add("부끄러운");
        adjectives.add("화난");
        adjectives.add("기쁜");
        adjectives.add("아프지 않은");
        adjectives.add("슬픈");
        adjectives.add("무서운");
        adjectives.add("지루한");
        adjectives.add("짜증나는");
        adjectives.add("피곤한");
        adjectives.add("깊은");
        adjectives.add("갈증나는");
        adjectives.add("힘든");
        adjectives.add("무거운");
        adjectives.add("가벼운");
        adjectives.add("신뢰할 수 있는");
        adjectives.add("혼란스러운");
        adjectives.add("날카로운");


        adjectives2.add("검은 빛의");
        adjectives2.add("푸른");
        adjectives2.add("붉은");
        adjectives2.add("주황색의");
        adjectives2.add("노오란");
        adjectives2.add("보라빛의");
        adjectives2.add("하얀");
        adjectives2.add("갈색의");
        adjectives2.add("회색빛의");
        adjectives2.add("금빛의");
        adjectives2.add("은빛의");
        adjectives2.add("동색의");
        adjectives2.add("적색의");

        List<String> animalNames = getStrings();

        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        int idx = random.nextInt(adjectives.size());
        builder.append(adjectives.get(idx));
        idx = random.nextInt(3);
        if (idx==1) {
            idx = random.nextInt(adjectives2.size());
            builder.append(" ").append(adjectives2.get(idx));
        }
        idx = random.nextInt(animalNames.size());
        builder.append(" ").append(animalNames.get(idx));

        return builder.toString();

    }

    private static List<String> getStrings() {
        List<String> animalNames = new ArrayList<>();

        // 동물 이름 추가
        animalNames.add("사자");
        animalNames.add("호랑이");
        animalNames.add("코끼리");
        animalNames.add("원숭이");
        animalNames.add("기린");
        animalNames.add("늑대");
        animalNames.add("여우");
        animalNames.add("곰");
        animalNames.add("사슴");
        animalNames.add("너구리");
        animalNames.add("토끼");
        animalNames.add("다람쥐");
        animalNames.add("오소리");
        animalNames.add("비버");
        animalNames.add("판다");
        animalNames.add("고릴라");
        animalNames.add("치타");
        animalNames.add("얼룩말");
        animalNames.add("낙타");
        animalNames.add("악어");
        animalNames.add("말");
        animalNames.add("양");
        animalNames.add("소");
        animalNames.add("돼지");
        animalNames.add("닭");
        animalNames.add("오리");
        animalNames.add("거위");
        animalNames.add("고양이");
        animalNames.add("강아지");
        animalNames.add("햄스터");
        animalNames.add("친칠라");
        animalNames.add("펭귄");
        animalNames.add("참새");
        animalNames.add("비둘기");
        animalNames.add("독수리");
        animalNames.add("까마귀");
        animalNames.add("독수리");
        animalNames.add("부엉이");
        animalNames.add("코뿔소");
        animalNames.add("표범");
        animalNames.add("늑대");
        animalNames.add("바다표범");
        animalNames.add("앵무새");
        animalNames.add("거북");
        animalNames.add("날다람쥐");
        animalNames.add("매");
        animalNames.add("오리너구리");
        animalNames.add("두더지");
        animalNames.add("쥐");
        animalNames.add("고슴도치");
        animalNames.add("도마뱀");
        animalNames.add("양");
        animalNames.add("파이프");
        animalNames.add("여우원숭이");
        animalNames.add("해마");
        animalNames.add("불곰");
        animalNames.add("고래");
        animalNames.add("고슴도치");
        animalNames.add("해달");
        animalNames.add("수달");
        animalNames.add("사막여우");
        animalNames.add("순록");
        animalNames.add("낙타");
        animalNames.add("악어");
        animalNames.add("하마");
        animalNames.add("늑대");
        animalNames.add("하마");
        animalNames.add("수달");
        animalNames.add("돼지");
        animalNames.add("라마");
        animalNames.add("알파카");
        animalNames.add("바다코끼리");
        animalNames.add("독수리");
        animalNames.add("참새");
        animalNames.add("펭귄");
        animalNames.add("토끼");
        animalNames.add("고양이");
        animalNames.add("강아지");
        animalNames.add("코뿔소");
        animalNames.add("표범");
        animalNames.add("악어");
        animalNames.add("원숭이");
        animalNames.add("판다");
        animalNames.add("곰");
        animalNames.add("백조");
        animalNames.add("랫서팬더");
        animalNames.add("하이에나");
        return animalNames;
    }
}