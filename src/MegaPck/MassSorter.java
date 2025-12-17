package MegaPck;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MassSorter {
    private static final double LOG_BASE_2 = Math.log(2);
    
    public static void sortWithProgress(List<Calibragem> list) {
        final long N = list.size();
        if (N == 0) return;

        // Estimativa para 184M registros (~N log N com fator empírico)
        final long totalOps = (long) (N * (Math.log(N) / LOG_BASE_2) * 11/10); 
        
        AtomicLong opsCount = new AtomicLong(0);
        long startTime = System.currentTimeMillis();
        
        // Thread de progresso otimizada
        Thread progressThread = new Thread(() -> {
            int lastPercent = -1;
            while (true) {
                long current = opsCount.get();
                int percent = (int) Math.min(99, (current * 100 / totalOps)); // Máx 99%
                
                if (percent != lastPercent) {
                    printEnhancedProgress(
                        percent, 
                        current, 
                        totalOps, 
                        startTime
                    );
                    lastPercent = percent;
                }
                
                if (current >= totalOps || Thread.currentThread().isInterrupted()) {
                    printEnhancedProgress(100, totalOps, totalOps, startTime);
                    break;
                }
                
                try {
                    Thread.sleep(1000); // Atualização a cada 1s para reduzir overhead
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        progressThread.setDaemon(true);
        progressThread.start();

        // Ordenação paralela (Java 8+)
        list.parallelStream()
            .sorted((a, b) -> {
                opsCount.incrementAndGet();
                return a.compareTo(b);
            })
            .collect(Collectors.toList());
        
        progressThread.interrupt();
    }

    private static void printEnhancedProgress(int percent, long current, long total, long startTime) {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = percent > 0 ? (elapsed * (100 - percent) / percent) : 0;
        
        String progressBar = String.format(
            "\r[%-50s] %d%% %s/%s ops | %ds elapsed | ~%ds remaining",
            "=".repeat(percent / 2) + ">",
            percent,
            formatNumber(current),
            formatNumber(total),
            elapsed,
            remaining
        );
        System.out.print(progressBar);
    }
    
    private static String formatNumber(long num) {
        return String.format("%,d", num);
    }
}