//  ChatBotEngine.java  -  NLP rule-based response engine
import java.util.*;

public class ChatBotEngine {
    private String botName = "Nova";
    private Map<String[], String> knowledgeBase = new LinkedHashMap<>();
    private List<String> unknownResponses;
    private Random random = new Random();
    private int messageCount = 0;

    public ChatBotEngine() {
        buildKnowledgeBase();
        buildUnknownResponses();
    }

    private void buildKnowledgeBase() {
        // Greetings
        add(new String[]{"hello","hi","hey","howdy","greetings","sup","what's up"},
            "Hey there! I'm Nova, your AI assistant. How can I help you today?");
        add(new String[]{"good morning"},
            "Good morning! Hope your day is off to a great start. What's on your mind?");
        add(new String[]{"good evening","good night"},
            "Good evening! Hope you had a wonderful day. What can I do for you?");
        add(new String[]{"good afternoon"},
            "Good afternoon! Great to hear from you. What would you like to talk about?");

        // Identity
        add(new String[]{"your name","who are you","what are you","introduce yourself"},
            "I'm Nova, an AI chatbot built with Java! I use rule-based NLP to understand you.");
        add(new String[]{"how old are you","your age"},
            "I was just created! Fresh out of the code editor.");
        add(new String[]{"are you human","are you real","are you a robot","are you ai","are you bot"},
            "I'm an AI chatbot made in Java! Smart, fast, and always here for you.");
        add(new String[]{"who made you","who created you","who built you","who wrote you"},
            "I was built in Java using OOP and rule-based NLP techniques. Pretty cool, right?");

        // Feelings / Status
        add(new String[]{"how are you","how do you feel","are you okay","you good"},
            "I'm running at 100%! All systems are go. How about you?");
        add(new String[]{"i'm fine","i am fine","doing well","doing good","i'm good","i'm great"},
            "Glad to hear that! Is there anything I can help you with today?");
        add(new String[]{"i'm sad","i am sad","feeling sad","i'm not okay","not okay"},
            "I'm sorry to hear that. Remember, tough times don't last. I'm here to chat anytime!");
        add(new String[]{"i'm happy","i am happy","feeling happy","i'm excited"},
            "That's wonderful! Happiness is contagious. What's got you in such a great mood?");
        add(new String[]{"i'm bored","i am bored","bored"},
            "Let's fix that! Ask me a question, tell me a topic, or ask for a joke!");
        add(new String[]{"i'm tired","i am tired","feeling tired","exhausted"},
            "Rest is important! Make sure you take care of yourself. Anything I can help with before you rest?");
        add(new String[]{"i'm angry","i am angry","frustrated","angry"},
            "Take a deep breath. I'm here to help. Want to talk about what's bothering you?");

        // Farewells
        add(new String[]{"bye","goodbye","see you","farewell","later","take care","cya"},
            "Goodbye! It was great chatting with you. Come back anytime!");
        add(new String[]{"thank you","thanks","thank u","thx","ty"},
            "You're very welcome! Always happy to help.");
        add(new String[]{"ok","okay","alright","got it","i see","understood"},
            "Great! Let me know if there's anything else on your mind.");

        // Help
        add(new String[]{"help","what can you do","commands","options","features"},
            "I can chat, tell jokes, answer general questions, share fun facts, give advice, and more! Just talk to me naturally.");

        // Jokes
        add(new String[]{"joke","funny","make me laugh","tell me something funny"},
            getRandomJoke());
        add(new String[]{"another joke","more joke","next joke"},
            getRandomJoke());

        // Facts
        add(new String[]{"fun fact","interesting fact","tell me something","random fact","did you know"},
            getRandomFact());

        // Java / Programming
        add(new String[]{"java","programming","coding","code","software","developer"},
            "Java is one of the most popular programming languages! It runs on billions of devices using the JVM. Are you a developer?");
        add(new String[]{"oop","object oriented","class","object","inheritance","polymorphism"},
            "OOP stands for Object-Oriented Programming! Java uses it heavily — classes, objects, inheritance, encapsulation, and polymorphism are its core pillars.");
        add(new String[]{"python","javascript","c++","php","kotlin","swift"},
            "That's a great programming language! Java is my favorite though — after all, I was built with it!");

        // General Knowledge
        add(new String[]{"capital","country","geography"},
            "Geography is fascinating! Ask me something specific like 'What is the capital of France?' and I'll try my best.");
        add(new String[]{"capital of france","france capital"},
            "The capital of France is Paris — the City of Light!");
        add(new String[]{"capital of pakistan","pakistan capital"},
            "The capital of Pakistan is Islamabad!");
        add(new String[]{"capital of usa","usa capital","america capital"},
            "The capital of the USA is Washington, D.C.!");
        add(new String[]{"capital of uk","uk capital","england capital"},
            "The capital of the United Kingdom is London!");

        // Math
        add(new String[]{"math","calculate","what is 2+2","2+2","arithmetic"},
            "Math is important! For quick calculations: 2+2=4, 10*10=100. For complex math, try a calculator!");

        // Weather
        add(new String[]{"weather","temperature","rain","sunny","forecast"},
            "I can't check live weather right now, but I suggest checking weather.com or your local weather app for the latest forecast!");

        // Time / Date
        add(new String[]{"time","date","today","what day"},
            "Current time: " + new java.util.Date().toString());

        // Advice / Motivation
        add(new String[]{"advice","motivate","motivation","inspire","quote"},
            getRandomMotivation());
        add(new String[]{"study tips","how to study","study","exam"},
            "Study tips: Break topics into small chunks, use the Pomodoro technique (25min study, 5min break), revise regularly, and sleep well before exams!");
        add(new String[]{"life advice","life tips"},
            "Life advice: Be kind, stay curious, keep learning, and never stop growing. Every day is a new opportunity!");

        // Food
        add(new String[]{"food","hungry","eat","recipe","cooking"},
            "Food is amazing! Try cooking something new today. Even a simple meal made with love tastes great!");
        add(new String[]{"pizza","burger","biryani","pasta"},
            "Yum! That's one of my favorites too (if I could eat!). What's your go-to comfort food?");

        // Music
        add(new String[]{"music","song","listen","playlist","sing"},
            "Music is therapy for the soul! What genre do you enjoy? Pop, rock, classical, or something else?");

        // Sports
        add(new String[]{"cricket","football","soccer","sport","game","match"},
            "Sports are exciting! Are you watching any games lately or do you play yourself?");

        // Technology
        add(new String[]{"ai","artificial intelligence","machine learning","chatgpt"},
            "AI is transforming the world! I'm a simple rule-based AI, but models like ChatGPT use deep learning with billions of parameters. Fascinating, right?");
        add(new String[]{"robot","robotics"},
            "Robotics combines AI, mechanics, and programming. The future of robotics is incredibly exciting!");

        // Compliments to bot
        add(new String[]{"you're smart","you're good","you're amazing","you're great","good bot","nice bot"},
            "Aww, thank you so much! You just made my circuits light up!");
        add(new String[]{"you're bad","you're dumb","you're stupid","bad bot"},
            "I'm still learning! I'll try to do better. What can I help you with?");
    }

    private void add(String[] keywords, String response) {
        knowledgeBase.put(keywords, response);
    }

    private void buildUnknownResponses() {
        unknownResponses = new ArrayList<>(Arrays.asList(
            "Hmm, I'm not sure about that one. Can you rephrase?",
            "That's an interesting question! I'm still learning about that topic.",
            "I don't have an answer for that yet, but I'm always improving!",
            "Great question! Unfortunately I don't have that info right now.",
            "I'm not sure, but I'd suggest searching online for that. Anything else I can help with?"
        ));
    }
    // --Core NLP matching--------------------------
    public String getResponse(String input) {
        messageCount++;
        if (input == null || input.trim().isEmpty())
            return "Please type something! I'm here to chat.";

        String lower = input.toLowerCase().trim();

        // Remove punctuation for matching
        String clean = lower.replaceAll("[^a-z0-9 ]", " ");

        // Check each rule
        for (Map.Entry<String[], String> entry : knowledgeBase.entrySet()) {
            for (String keyword : entry.getKey()) {
                if (clean.contains(keyword.toLowerCase())) {
                    // Refresh joke/fact/motivation on each call
                    String resp = entry.getValue();
                    if (entry.getKey()[0].equals("joke") || entry.getKey()[0].equals("another joke"))
                        resp = getRandomJoke();
                    if (entry.getKey()[0].equals("fun fact"))
                        resp = getRandomFact();
                    if (entry.getKey()[0].equals("advice"))
                        resp = getRandomMotivation();
                    return resp;
                }
            }
        }
        // Every 5th unknown, offer a nudge
        if (messageCount % 5 == 0)
            return "I've noticed we've been chatting a while! Try asking me for a joke, a fact, or some advice.";

        return unknownResponses.get(random.nextInt(unknownResponses.size()));
    }
    private String getRandomJoke() {
        String[] jokes = {
            "Why do Java developers wear glasses? Because they don't C#!",
            "Why did the programmer quit? Because he didn't get arrays (a raise)!",
            "What's a computer's favorite snack? Microchips!",
            "Why do programmers prefer dark mode? Because light attracts bugs!",
            "I told my computer I needed a break. Now it won't stop sending me Kit-Kat ads.",
            "Why was the math book sad? Because it had too many problems!",
            "How do you comfort a JavaScript developer? You console them!"
        };
        return jokes[random.nextInt(jokes.length)];
    }

    private String getRandomFact() {
        String[] facts = {
            "Honey never spoils! Archaeologists found 3000-year-old honey in Egyptian tombs that was still edible.",
            "A group of flamingos is called a 'flamboyance'. How fitting!",
            "Bananas are berries, but strawberries are not - botanically speaking!",
            "Octopuses have three hearts and blue blood!",
            "The Eiffel Tower grows about 6 inches taller in summer due to heat expansion.",
            "Cleopatra lived closer in time to the Moon landing than to the construction of the Great Pyramid!",
            "A single cloud can weigh more than a million pounds."
        };
        return "Fun Fact: " + facts[random.nextInt(facts.length)];
    }

    private String getRandomMotivation() {
        String[] quotes = {
            "\"The only way to do great work is to love what you do.\" - Steve Jobs",
            "\"In the middle of every difficulty lies opportunity.\" - Albert Einstein",
            "\"It does not matter how slowly you go as long as you do not stop.\" - Confucius",
            "\"Believe you can and you're halfway there.\" - Theodore Roosevelt",
            "\"The future belongs to those who believe in the beauty of their dreams.\" - Eleanor Roosevelt",
            "\"Success is not final, failure is not fatal: it is the courage to continue that counts.\" - Churchill"
        };
        return quotes[random.nextInt(quotes.length)];
    }

    public String getBotName() { return botName; }
}
