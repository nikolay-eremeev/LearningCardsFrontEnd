package com.example.learningcards;

import java.util.ArrayList;
import java.util.Random;

class LearningCards implements ILearningCards {
    private static final String TAG = "LearningCards";
    private ArrayList<Card> cardsArrayList = new ArrayList<>();
    private Card activeCard;
    private int activeCardNumber;
    private int activeWordNumber;

    LearningCards() {

        String[] input = ("English\tRussian\thurricane damage poses a major threat to many coastal communities\tущерб от урагана представляет серьезную угрозу для многих прибрежных сообществ\n" +
                "English\tRussian\tthe reports of the commission are often referred to in the media\tна сообщения комиссии часто ссылаются в средствах массовой информации\n" +
                "English\tRussian\tthe sport gives an enormous amount of pleasure to many people\tспорт доставляет огромное количество удовольствия многим людям\n" +
                "English\tRussian\the managed to spill a little lake of alcohol on the bookshelf\tему удалось пролить небольшое озеро алкоголя на книжную полку\n" +
                "English\tRussian\twe look forward to a very rewarding relationship with them\tмы с нетерпением ждем очень полезных отношений с ними\n" +
                "English\tRussian\tit is a puzzle that any intelligent child could solve\tэто головоломка, которую может решить любой умный ребенок\n" +
                "English\tRussian\the was driving in a manner likely to endanger life\tон ехал в манере, способной поставить под угрозу жизнь\n" +
                "English\tRussian\the conducts research in the field of nuclear physics\tон проводит исследования в области ядерной физики\n" +
                "English\tRussian\tthe kids are mad keen on computer games at the moment\tдети без ума от компьютерных игр в данный момент\n" +
                "English\tRussian\texistence is about to be threatened by a strange new blight\tсуществованию грозит новый странный упадок\n" +
                "English\tRussian\tthese exercises are good for building up leg strength\tэти упражнения полезны для наращивания силы ног\n" +
                "English\tRussian\twhen they grow up they will be able to swim like us\tкогда они вырастут, они смогут плавать, как мы\n" +
                "English\tRussian\tshe realized that, he did not seem at all upset\tона поняла, что он, похоже, совсем не расстроился\n" +
                "English\tRussian\tthousands of people bet on the result of the match\tтысячи людей сделали ставку на результат матча\n" +
                "English\tRussian\tthe handwriting on the letter was neat and feminine\tпочерк на письме был аккуратным и женственным\n" +
                "English\tRussian\tthe conditions for cooperation were clearly defined\tусловия сотрудничества были четко определены\n" +
                "English\tRussian\tthe government is keen to encourage investment\tправительство стремится стимулировать инвестиции\n" +
                "English\tRussian\twe collect data on the state of the environment\tмы собираем данные о состоянии окружающей среды\n" +
                "English\tRussian\tvegetables should be stored in a cool dry place\tовощи следует хранить в сухом прохладном месте\n" +
                "English\tRussian\tThe man hesitated, his eyes flicking nervously\tМужчина колебался, его глаза нервно щурелились\n" +
                "English\tRussian\tthis has been a quite fantastically rewarding experience\tэто был довольно фантастический опыт\n" +
                "English\tRussian\tan example of a shuffle is a sliding step in a dance\tпример шаффла - скользящий шаг в танце").split("\n");

        for (String str : input) {
            cardsArrayList.add(new Card(str.split("\t")));
        }

        this.pickWordToLearn();
    }

    private void pickWordToLearn() {

        activeCardNumber = (new Random()).nextInt(cardsArrayList.size());
        activeCard = cardsArrayList.get(activeCardNumber);
        activeWordNumber = (new Random()).nextInt(2);

    }

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

    @Override
    public String getWord() {
        return activeCard.getWord(activeWordNumber);
    }

    @Override
    public String getWordLanguage() {
        return activeCard.getWordLanguage(activeWordNumber);
    }

    @Override
    public String getTranslation() {
        return activeCard.getWord(1 - activeWordNumber);
    }

    @Override
    public String getTranslationLanguage() {
        return activeCard.getTranslationLanguage(1 - activeWordNumber);
    }

    @Override
    public int getScore() {
        return activeCard.getScore();
    }

    @Override
    public boolean pushAnswer(String userAnswer) {

        String userTextTmp = formatString(userAnswer);
        String translation = formatString(this.getTranslation());
        boolean isAnswerRight = userTextTmp.equals(translation);

        activeCard.pushAnswer(isAnswerRight);

        cardsArrayList.set(activeCardNumber, activeCard);
        this.pickWordToLearn();

        return isAnswerRight;
    }

}


