import java.util.*;

public final class CareerProfile {

    public static final String[] DOMAINS = {
        "Software Engineering", "Data Science", "Data Analytics", "AI / ML",
        "Web Development", "Cybersecurity", "Cloud / DevOps", "UI/UX + Product",
        "Robotics", "Embedded Systems", "Blockchain", "Game Development",
        "Network Engineering", "Cloud Architecture", "Quantitative Finance",
        "Product Management"
    };

    public record Option(String icon, String title, String description, int[] weights) {}
    public record Question(String text, String description, Option[] options) {}
    public record Match(String domain, int percent, int score) {}
    public record Result(List<Match> matches, int[] scores) {}

    /*
     * 20 questions. Each option contributes a different signal to the 16 domains.
     * Domain order is the same as DOMAINS above.
     */
    public static final Question[] QUESTIONS = {

        q("Which kind of problem would you most enjoy solving?",
          "Pick the problem that naturally pulls your attention.",
          o("⌘","Building a useful application","Turning an idea into a working system.", w(5,0,0,1,3,0,2,1,2,2,1,3,1,1,0,2)),
          o("▥","Finding patterns in data","Exploring information to discover what it means.", w(1,5,4,4,0,0,0,0,0,0,0,0,0,0,5,1)),
          o("◈","Investigating a security problem","Understanding what went wrong and how to stop it.", w(2,0,0,0,0,5,4,0,1,1,2,0,4,2,1,0)),
          o("✦","Designing a great experience","Making technology simple, useful and enjoyable.", w(1,0,2,1,4,0,1,5,0,0,0,2,0,0,0,5))),

        q("What sounds most satisfying?",
          "Think about the activity you could spend hours doing.",
          o("</>","Writing and improving code","Solving logic problems by building software.", w(5,1,1,2,4,1,3,1,3,3,2,3,2,2,1,2)),
          o("◒","Exploring a spreadsheet or dataset","Finding trends, outliers and useful conclusions.", w(1,5,5,4,0,0,0,0,0,0,0,0,0,0,4,2)),
          o("⌁","Taking apart a system","Understanding how systems work and where they can fail.", w(3,1,1,1,0,5,4,0,3,3,2,0,5,4,1,1)),
          o("✎","Sketching an interface","Thinking about users, layout and interaction.", w(1,0,1,1,5,0,1,5,0,0,0,2,0,0,0,5))),

        q("How do you feel about mathematics?",
          "There is no correct answer — choose your natural preference.",
          o("∑","I genuinely enjoy it","Equations, probability and quantitative reasoning are fun.", w(2,5,3,5,0,0,0,0,2,2,2,0,1,1,5,2)),
          o("≈","I like practical math","I enjoy numbers when they solve a real problem.", w(2,3,5,3,0,0,1,1,2,2,1,0,1,2,4,3)),
          o("○","I can work with it when needed","I prefer logic or practical experimentation.", w(4,1,2,2,2,1,2,2,4,4,2,3,2,2,2,3)),
          o("→","I prefer very little math","I would rather focus on people, products or interfaces.", w(1,0,1,0,4,1,1,5,0,0,0,3,0,0,0,5))),

        q("Which environment sounds exciting?",
          "Imagine your ideal technology workplace.",
          o("⚙","A product engineering team","Shipping features and improving reliable systems.", w(5,1,1,2,4,1,4,2,3,3,1,2,2,3,0,4)),
          o("🔬","A research or experimentation team","Testing hypotheses and discovering new patterns.", w(1,5,3,5,0,0,0,1,3,2,2,0,0,1,5,2)),
          o("🛡","A security operations environment","Monitoring, investigating and responding to threats.", w(2,0,1,1,0,5,5,0,2,2,2,0,5,4,1,1)),
          o("🎨","A design/product studio","Working closely with users and shaping experiences.", w(1,0,2,1,4,0,1,5,0,0,0,3,0,1,0,5))),

        q("When you learn something new, what do you do first?",
          "Choose the approach that feels most natural.",
          o("🧩","Build a small project","I learn by making something and debugging it.", w(5,1,1,2,4,1,3,2,4,4,2,3,2,3,1,3)),
          o("📚","Read the theory first","I want to understand why it works.", w(2,4,2,5,1,1,1,1,3,3,3,1,1,2,5,2)),
          o("🔎","Experiment with examples","I learn by changing variables and observing results.", w(2,5,4,5,1,2,2,1,4,3,2,2,2,2,4,2)),
          o("👥","Discuss it with people","I learn through conversations and different perspectives.", w(1,1,3,1,2,1,1,5,0,0,0,2,1,1,1,5))),

        q("A project suddenly breaks. What is your instinct?",
          "Imagine you are responsible for the result.",
          o("🧠","Debug it step by step","Trace the logic until you find the root cause.", w(5,2,2,3,3,3,4,0,4,4,2,2,3,4,2,2)),
          o("📉","Inspect the data","Look for anomalies or a pattern in what changed.", w(2,5,5,4,1,1,2,0,1,1,1,0,1,1,4,2)),
          o("🚨","Investigate the threat","Check whether the failure is a security incident.", w(2,0,1,1,0,5,5,0,1,2,2,0,5,4,1,1)),
          o("🗣","Ask the users","Understand what people experienced before changing things.", w(1,0,2,1,2,0,1,5,0,0,0,1,0,0,0,5))),

        q("Which result would make you proudest?",
          "Think about the impact you want your work to have.",
          o("🚀","A system used by thousands of people","I built the technical foundation behind it.", w(5,1,1,2,4,1,4,2,2,2,1,3,2,4,1,4)),
          o("💡","A discovery nobody noticed before","My analysis revealed something important.", w(1,5,5,5,0,0,0,1,0,0,0,0,0,1,5,2)),
          o("🔐","Stopping a serious cyber incident","My investigation protected an organization.", w(2,0,1,1,0,5,5,0,1,1,2,0,5,4,1,1)),
          o("❤️","Making a product easier to use","People enjoy using something I helped shape.", w(1,0,2,1,5,0,1,5,0,0,0,2,0,1,0,5))),

        q("How much do you enjoy working with hardware?",
          "Think beyond screens and software.",
          o("🖥","Mostly software","I prefer code, applications and digital systems.", w(5,2,2,2,4,1,3,2,1,1,1,2,1,2,1,3)),
          o("⚙","I like devices","Sensors, microcontrollers and physical computing interest me.", w(3,1,1,3,1,1,2,0,5,5,2,2,2,2,1,1)),
          o("🤖","I love robots","I want software and physical systems to work together.", w(3,1,1,4,1,1,2,0,5,4,1,2,2,2,1,1)),
          o("🎮","Interactive hardware is cool","Controllers, graphics and experiences appeal to me.", w(2,0,1,2,3,0,1,3,3,3,1,5,1,1,1,3))),

        q("What kind of uncertainty do you enjoy?",
          "Choose the uncertainty that feels like an interesting challenge.",
          o("📊","Messy information","I enjoy turning unclear data into a clear conclusion.", w(1,5,5,4,0,0,0,0,0,0,0,0,0,1,4,2)),
          o("🧩","An unknown system","I enjoy figuring out how something works.", w(5,2,2,3,1,5,4,0,3,3,2,2,4,4,2,1)),
          o("📈","A changing market","I enjoy probabilities, risk and quantitative decisions.", w(1,4,4,3,0,0,0,0,0,0,1,0,0,1,5,4)),
          o("👤","What users really need","I enjoy discovering needs that people cannot always articulate.", w(1,0,3,1,4,0,1,5,0,0,0,2,0,0,0,5))),

        q("Which tool category would you most likely explore voluntarily?",
          "Pick the one you would open out of curiosity.",
          o("💻","Code editor / IDE","Building, refactoring and experimenting with code.", w(5,1,1,2,4,1,3,1,3,3,2,3,2,2,1,2)),
          o("📊","Notebook / analytics tool","Charts, datasets, statistics and experiments.", w(1,5,5,5,0,0,0,0,0,0,0,0,0,1,4,2)),
          o("🛡","Security / network tools","Logs, scanners, traffic and system evidence.", w(2,0,1,1,0,5,5,0,2,2,5,0,5,4,1,1)),
          o("🎨","Design / product tools","Wireframes, prototypes, user flows and product ideas.", w(1,0,2,1,4,0,1,5,0,0,0,2,0,1,0,5))),

        q("Which statement sounds most like you?",
          "Go with your first reaction.",
          o("🔧","I like making things work","I get satisfaction from implementation and systems.", w(5,2,2,2,4,2,4,1,4,4,2,3,3,4,1,3)),
          o("🔬","I like figuring things out","Questions and patterns keep me curious.", w(2,5,4,5,0,1,1,1,2,2,1,0,1,1,5,2)),
          o("🕵","I notice what others miss","I naturally look for clues, weaknesses and anomalies.", w(2,1,2,2,0,5,4,1,1,2,2,0,5,4,2,1)),
          o("✨","I notice how things feel","I care about clarity, usability and the human side.", w(1,0,2,1,5,0,1,5,0,0,0,3,0,0,0,5))),

        q("How do you feel about repetitive work?",
          "Think about long projects and day-to-day work.",
          o("⚡","Automate it","If I repeat something, I want to build a system for it.", w(5,2,2,3,3,2,5,1,3,4,3,2,3,5,2,4)),
          o("📋","Analyze it","Repetition can reveal useful patterns in the data.", w(1,5,5,4,0,0,1,0,0,0,0,0,0,1,4,2)),
          o("🔍","Investigate it","Repeated anomalies may indicate a deeper problem.", w(2,2,2,2,0,5,4,0,1,1,2,0,5,4,1,1)),
          o("🎯","Improve the experience","I want to redesign the process so it feels better.", w(1,0,3,1,4,0,2,5,0,0,0,2,0,1,0,5))),

        q("What kind of output do you enjoy creating?",
          "Choose the output you would be excited to show someone.",
          o("🧱","A working application","Something people can actually use.", w(5,1,1,2,5,1,3,2,2,2,1,3,1,3,1,4)),
          o("📈","A clear analysis","A visual or report that explains what the data says.", w(1,5,5,4,0,0,1,1,0,0,0,0,0,1,4,3)),
          o("🛡","A secure system","A system that is resilient against attacks.", w(3,0,1,2,0,5,5,0,1,2,3,0,5,4,1,2)),
          o("🎨","A beautiful product","A polished interface or product experience.", w(1,0,2,1,5,0,1,5,0,0,0,3,0,1,0,5))),

        q("Which type of responsibility sounds best?",
          "Imagine you own this area of a project.",
          o("🏗","Technical architecture","I decide how software and systems should be built.", w(5,1,1,3,3,1,5,1,3,3,2,2,2,5,1,4)),
          o("📊","Analytical conclusions","People depend on me to explain what the data means.", w(1,5,5,4,0,0,1,0,0,0,0,0,0,1,4,3)),
          o("🛡","Risk and security","I protect systems and respond when something goes wrong.", w(3,0,1,1,0,5,5,0,2,2,2,0,5,5,1,2)),
          o("🚀","Product direction","I decide what to build and why it matters.", w(2,0,3,1,3,0,2,5,0,0,0,2,0,2,1,5))),

        q("What kind of feedback motivates you?",
          "Think about what makes you want to improve.",
          o("🐛","Your code works better now","Technical quality and performance feedback.", w(5,2,1,2,4,1,4,1,3,3,2,3,2,3,1,2)),
          o("📈","Your model found something useful","Evidence that analysis or prediction improved.", w(1,5,4,5,0,0,1,0,0,0,0,0,0,1,5,2)),
          o("🚨","You caught a real vulnerability","Proof that your investigation prevented risk.", w(2,0,1,1,0,5,5,0,1,1,2,0,5,4,1,1)),
          o("❤️","Users love the experience","Real people find the product easier or better.", w(1,0,3,1,5,0,1,5,0,0,0,3,0,1,0,5))),

        q("Which statement about creativity fits you?",
          "Creativity can mean very different things in technology.",
          o("🧠","Creative problem solving","I like finding clever technical solutions.", w(5,2,2,3,4,1,3,2,3,3,2,3,2,2,2,3)),
          o("🔬","Creative experimentation","I like forming unusual hypotheses and testing them.", w(2,5,4,5,0,0,0,1,2,2,2,1,0,1,5,2)),
          o("🎨","Visual creativity","I like composition, interfaces and product experiences.", w(1,0,2,1,5,0,1,5,0,0,0,3,0,0,0,5)),
          o("🧩","System creativity","I like designing architectures and processes.", w(5,1,2,3,2,2,5,1,3,3,3,2,3,5,2,4))),

        q("If you had a free weekend, what would you build or explore?",
          "Pick the project you would be most tempted to start.",
          o("🌐","A website or app","Something interactive that others can use.", w(5,1,1,2,5,1,3,2,1,1,1,3,1,2,1,4)),
          o("🤖","An AI experiment","Train, test or compare a model.", w(2,4,3,5,0,0,1,0,2,2,1,1,0,1,4,2)),
          o("🔐","A security lab","Explore networks, vulnerabilities or defenses.", w(3,1,1,2,0,5,5,0,2,2,3,0,5,4,1,1)),
          o("🎮","A game or interactive experience","Mix creativity, systems and user experience.", w(3,0,1,2,3,0,1,3,2,2,1,5,1,1,1,3))),

        q("How do you prefer making decisions?",
          "Choose the decision style that sounds most natural.",
          o("📐","Logic and evidence","I want a clear technical basis for the decision.", w(5,4,4,4,1,2,4,1,2,2,2,1,3,4,4,3)),
          o("📊","Numbers and probabilities","I want measurable evidence and expected outcomes.", w(1,5,5,4,0,0,1,0,0,0,0,0,0,1,5,4)),
          o("🧭","Risk and threat analysis","I think about what could go wrong.", w(3,1,2,2,0,5,5,0,1,1,2,0,5,4,2,2)),
          o("👥","People and context","I consider users, goals and practical constraints.", w(1,0,3,1,4,0,2,5,0,0,0,2,0,1,0,5))),

        q("Which scale sounds most exciting?",
          "Choose the system or impact you would like to work with.",
          o("🏢","A large software platform","Millions of users, complex systems and reliability.", w(5,1,1,2,4,1,5,1,2,2,1,2,3,5,1,4)),
          o("🧮","A massive dataset","Huge amounts of information waiting for insight.", w(1,5,5,5,0,0,1,0,0,0,0,0,0,1,5,2)),
          o("🌐","Global infrastructure","Networks, cloud systems and distributed reliability.", w(3,1,1,2,1,3,5,0,1,2,2,0,5,5,1,3)),
          o("🚀","A product people love","A focused product with strong user impact.", w(2,0,3,1,4,0,2,5,0,0,0,2,0,1,0,5))),

        q("What would you most like to become known for?",
          "This is about the identity of your future work.",
          o("💻","Being a strong builder","People trust me to turn ideas into working technology.", w(5,1,1,2,4,1,4,1,3,3,2,3,2,3,1,3)),
          o("🔬","Being a sharp problem solver","People trust my analysis and reasoning.", w(2,5,5,5,0,1,1,0,2,2,1,0,1,1,5,2)),
          o("🛡","Being the person who finds the weakness","People rely on me to protect systems.", w(2,1,1,1,0,5,5,0,1,2,2,0,5,4,1,1)),
          o("🎨","Being the person who understands users","People rely on me to make products useful and intuitive.", w(1,0,3,1,5,0,1,5,0,0,0,2,0,1,0,5)))
    };

    private static Question q(String text, String desc, Option... options) {
        return new Question(text, desc, options);
    }

    private static Option o(String icon, String title, String desc, int[] weights) {
        return new Option(icon, title, desc, weights);
    }

    private static int[] w(int... values) { return values; }

    public static Result calculate(int[] answers) {
        int[] scores = new int[DOMAINS.length];

        for (int q = 0; q < QUESTIONS.length && q < answers.length; q++) {
            int a = answers[q];
            if (a < 0 || a >= QUESTIONS[q].options.length) continue;

            int[] weights = QUESTIONS[q].options[a].weights;
            for (int d = 0; d < scores.length; d++) {
                scores[d] += weights[d];
            }
        }

        int maxPossible = 0;
        for (Question question : QUESTIONS) {
            int max = 0;
            for (Option option : question.options) {
                for (int value : option.weights) max = Math.max(max, value);
            }
            maxPossible += max;
        }

        List<Match> matches = new ArrayList<>();
        for (int d = 0; d < DOMAINS.length; d++) {
            int percent = maxPossible == 0 ? 0 :
                    Math.min(99, Math.max(1, Math.round(scores[d] * 100f / maxPossible)));
            matches.add(new Match(DOMAINS[d], percent, scores[d]));
        }

        matches.sort((a,b) -> Integer.compare(b.score(), a.score()));
        return new Result(matches, scores);
    }
}
