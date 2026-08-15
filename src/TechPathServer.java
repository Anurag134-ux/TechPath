import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class TechPathServer {

    private static final Path PUBLIC = Path.of("public");

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0
        );

        server.createContext("/", TechPathServer::staticFile);
        server.createContext("/assessment", TechPathServer::assessment);
        server.createContext("/question", TechPathServer::question);
        server.createContext("/result", TechPathServer::result);

        System.out.println("TechPath running on port " + port);
        System.out.println("HTML + CSS + Java only — no JavaScript.");
        server.start();
    }

    private static void assessment(HttpExchange ex) throws IOException {
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            send(ex, 405, "text/plain", "Method Not Allowed");
            return;
        }
        send(ex, 200, "text/html; charset=UTF-8", renderQuestion(0, emptyAnswers()));
    }

    private static void question(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            send(ex, 405, "text/plain", "Method Not Allowed");
            return;
        }

        Map<String,String> form = parseForm(readBody(ex));
        int current = integer(form.get("q"), 0);
        int[] answers = readAnswers(form);

        String answer = form.get("answer");
        if (answer != null) answers[current] = integer(answer, -1);

        String nav = form.get("nav");

        if ("back".equals(nav)) {
            current = Math.max(0, current - 1);
        } else {
            if (answers[current] < 0 || answers[current] > 3) {
                send(ex, 400, "text/html; charset=UTF-8",
                        errorPage("Choose one option before continuing."));
                return;
            }

            if (current == CareerProfile.QUESTIONS.length - 1) {
                send(ex, 200, "text/html; charset=UTF-8", resultPage(answers));
                return;
            }
            current++;
        }

        send(ex, 200, "text/html; charset=UTF-8", renderQuestion(current, answers));
    }

    private static void result(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            send(ex, 405, "text/plain", "Method Not Allowed");
            return;
        }
        send(ex, 200, "text/html; charset=UTF-8",
                resultPage(readAnswers(parseForm(readBody(ex)))));
    }

    private static String renderQuestion(int index, int[] answers) {
        CareerProfile.Question q = CareerProfile.QUESTIONS[index];

        int percent = Math.round((index + 1) * 100f / CareerProfile.QUESTIONS.length);
        StringBuilder options = new StringBuilder();

        for (int i = 0; i < q.options().length; i++) {
            CareerProfile.Option o = q.options()[i];
            String checked = answers[index] == i ? " checked" : "";

            options.append("""
                <label class="option">
                    <input type="radio" name="answer" value="%d"%s>
                    <span class="option-content">
                        <span class="option-icon">%s</span>
                        <span class="option-text">
                            <strong>%s</strong>
                            <small>%s</small>
                        </span>
                        <span class="option-check">✓</span>
                    </span>
                </label>
                """.formatted(i, checked, esc(o.icon()), esc(o.title()), esc(o.description())));
        }

        for (int i = 0; i < answers.length; i++) {
            if (answers[i] >= 0) {
                options.append("<input type=\"hidden\" name=\"q%d\" value=\"%d\">".formatted(i, answers[i]));
            }
        }

        String backDisabled = index == 0 ? "disabled" : "";
        String next = index == CareerProfile.QUESTIONS.length - 1 ? "See My Results" : "Next Question";

        return assessmentTemplate().formatted(
                String.format("%02d", index + 1),
                percent,
                percent,
                String.format("%02d", index + 1),
                esc(q.text()),
                esc(q.description()),
                index,
                options.toString(),
                backDisabled,
                next
        );
    }

    private static String resultPage(int[] answers) {
        CareerProfile.Result result = CareerProfile.calculate(answers);
        CareerProfile.Match top = result.matches().get(0);

        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < Math.min(8, result.matches().size()); i++) {
            CareerProfile.Match m = result.matches().get(i);
            rows.append("""
                <article class="match-card">
                    <div class="rank">%02d</div>
                    <div class="match-main">
                        <h3>%s</h3>
                        <div class="match-bar"><span style="width:%d%%"></span></div>
                    </div>
                    <strong>%d%%</strong>
                </article>
                """.formatted(i + 1, esc(m.domain()), m.percent(), m.percent()));
        }

        return RESULT_TEMPLATE.formatted(
                esc(top.domain()), esc(top.domain()), top.percent(), rows.toString()
        );
    }

    private static String assessmentTemplate() {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TechPath — Assessment</title>
<link rel="stylesheet" href="/style.css">
</head>
<body>
<header class="navbar">
<a class="brand" href="/"><span class="brand-mark">↗</span><span>TechPath</span></a>
<nav><a href="/">Home</a><a class="active" href="/assessment">Assessment</a><a href="/#domains">Domains</a><a href="/#roadmaps">Roadmaps</a><a href="/#about">About Us</a></nav>
<a class="sign-in" href="/">Exit</a>
</header>

<main class="assessment-container">
<section class="assessment-header">
<div>
<p class="eyebrow">— TECHPATH DISCOVERY ASSESSMENT</p>
<h1>Let's understand <span>how you think.</span></h1>
<p class="assessment-intro">20 questions. Four choices each. There are no right or wrong answers — choose what feels most like you.</p>
</div>
<div class="question-count"><strong>%s</strong><span>/ 20</span></div>
</section>

<div class="progress-area">
<div class="progress-label"><span>Progress</span><span>%d%%</span></div>
<div class="progress-track"><div class="progress-fill" style="width:%d%%"></div></div>
</div>

<section class="question-card">
<div class="question-number">QUESTION %s</div>
<h2>%s</h2>
<p class="question-description">%s</p>

<form class="options" method="post" action="/question">
<input type="hidden" name="q" value="%d">
%s
<div class="question-navigation">
<button class="back-button" type="submit" name="nav" value="back" %s>← Back</button>
<button class="next-button" type="submit" name="nav" value="next">%s <span>→</span></button>
</div>
</form>
</section>

<div class="assessment-note"><span>✦</span>Your answers are processed by the Java recommendation engine.</div>
</main>

<footer><span>TECHPATH</span><span>Find the domain that fits the way you think.</span></footer>
</body>
</html>
""";
    }

    private static final String RESULT_TEMPLATE = """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TechPath — Your Results</title>
<link rel="stylesheet" href="/style.css">
</head>
<body>
<header class="navbar">
<a class="brand" href="/"><span class="brand-mark">↗</span><span>TechPath</span></a>
<nav><a href="/">Home</a><a class="active" href="/assessment">Assessment</a><a href="/#domains">Domains</a><a href="/#roadmaps">Roadmaps</a><a href="/#about">About Us</a></nav>
<a class="sign-in" href="/assessment">Retake</a>
</header>

<main class="result-container">
<section class="result-hero">
<p class="eyebrow">— YOUR TECH PROFILE</p>
<h1>Your strongest path is <span>%s</span></h1>
<p>Based on your answers, TechPath found the strongest alignment between your interests, thinking style and preferred technology problems.</p>

<div class="top-match">
<div class="top-match-label">TOP MATCH</div>
<h2>%s</h2>
<div class="percentage">%d%% MATCH</div>
</div>
</section>

<section class="matches">
<div class="matches-title">YOUR TOP 8 CAREER MATCHES</div>
%s
</section>

<div class="result-actions">
<a class="cta" href="/assessment">Retake Assessment →</a>
<a class="outline-button" href="/#roadmaps">Explore Roadmaps</a>
</div>
</main>

<footer><span>TECHPATH</span><span>Find the domain that fits the way you think.</span></footer>
</body>
</html>
""";

    private static String staticFile(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if ("/".equals(path)) path = "/index.html";

        Path file = PUBLIC.resolve(path.substring(1)).normalize();
        if (!file.startsWith(PUBLIC) || !Files.exists(file) || Files.isDirectory(file)) {
            send(ex, 404, "text/plain; charset=UTF-8", "404 — Page not found");
            return null;
        }

        byte[] data = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", contentType(file));
        ex.sendResponseHeaders(200, data.length);
        try (OutputStream out = ex.getResponseBody()) { out.write(data); }
        return null;
    }

    private static int[] emptyAnswers() {
        int[] a = new int[CareerProfile.QUESTIONS.length];
        Arrays.fill(a, -1);
        return a;
    }

    private static int[] readAnswers(Map<String,String> form) {
        int[] answers = emptyAnswers();
        for (int i = 0; i < answers.length; i++) {
            answers[i] = integer(form.get("q" + i), -1);
        }
        return answers;
    }

    private static Map<String,String> parseForm(String body) {
        Map<String,String> map = new HashMap<>();
        if (body == null || body.isBlank()) return map;

        for (String pair : body.split("&")) {
            String[] p = pair.split("=", 2);
            String key = URLDecoder.decode(p[0], StandardCharsets.UTF_8);
            String value = p.length > 1 ? URLDecoder.decode(p[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream in = ex.getRequestBody()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static int integer(String value, int fallback) {
        try { return value == null ? fallback : Integer.parseInt(value); }
        catch (NumberFormatException e) { return fallback; }
    }

    private static void send(HttpExchange ex, int code, String type, String body) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream out = ex.getResponseBody()) { out.write(data); }
    }

    private static String contentType(Path p) {
        String n = p.toString().toLowerCase();
        if (n.endsWith(".css")) return "text/css; charset=UTF-8";
        if (n.endsWith(".html")) return "text/html; charset=UTF-8";
        return "application/octet-stream";
    }

    private static String esc(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }

    private static String errorPage(String message) {
        return """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TechPath</title><link rel="stylesheet" href="/style.css"></head>
<body><main class="assessment-container"><section class="question-card">
<p class="eyebrow">— ONE MORE STEP</p><h2>%s</h2><br><a class="cta" href="/assessment">Back to Assessment →</a>
</section></main></body></html>
""".formatted(esc(message));
    }
}