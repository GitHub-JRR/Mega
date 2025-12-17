package MegaPck;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ProgressTracker {
    private final int totalItems;
    private final Instant startTime;
    
    
    public ProgressTracker(int totalItems) {
        this.totalItems = totalItems;
        this.startTime = Instant.now();
    }
    
    public void print(int currentNumber) {
        // Calcula percentual
        double percent = (double) currentNumber / totalItems * 100;
        
        // Calcula tempos
        Instant now = Instant.now();
        Duration elapsed = Duration.between(startTime, now);
        Duration remaining = Duration.ofSeconds(
            (long) (elapsed.getSeconds() * (totalItems - currentNumber) / Math.max(1, currentNumber)));
        
        // Calcula tempo médio por item
        double avgTimePerItem = elapsed.getSeconds() / (double) Math.max(1, currentNumber);
        
        // Calcula horário previsto para término
        Instant estimatedEnd = now.plus(remaining);
        LocalTime endTime = LocalTime.ofInstant(estimatedEnd, ZoneId.systemDefault());
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // Formata saída
        String progressBar = buildProgressBar(percent);
        String elapsedStr = formatDuration(elapsed);
        String remainingStr = formatDuration(remaining);
        
        System.out.printf("\r:%5.1f%%|%s| %d/%d [%s<%s, %.2fs/it] %s",
            percent,
            progressBar,
            currentNumber,
            totalItems,
            elapsedStr,
            remainingStr,
            avgTimePerItem,
            endTimeStr);
        System.out.flush();
        GerenciadorMensagens.setMsgPrefix();
    }
    
    private String buildProgressBar(double percent) {
        int width = 50;
        int filled = (int) (percent / 100 * width);
        return "█".repeat(filled) + " ".repeat(width - filled);
    }
    
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    /*
    public static void main(String[] args) throws InterruptedException {
        // Exemplo de uso
        ProgressTracker tracker = new ProgressTracker(100);
        
        for (int i = 1; i <= 100; i++) {
            Thread.sleep(160); // Simula processamento
            tracker.print(i);
        }
    }
    */
}
