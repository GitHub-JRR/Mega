package MegaPck;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CalibragemSorter {

    public static void sortWithProgress(List<Calibragem> list) {
        // Contador atômico para acompanhar o progresso
        AtomicInteger progress = new AtomicInteger(0);
        final int total = list.size();

        // Thread para mostrar o progresso
        Thread progressThread = new Thread(() -> {
            int lastProgress = -1;
            while (true) {
                int current = progress.get();
                int percent = (int) ((current / (double) total) * 100);

                if (percent != lastProgress) {
                    printProgressBar(percent);
                    lastProgress = percent;
                }

                if (current >= total) {
                    printProgressBar(100);
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        progressThread.start();

        // Ordenação com acompanhamento de progresso
        Collections.sort(list, (a, b) -> {
            int result = a.compareTo(b);
            progress.incrementAndGet();
            return result;
        });

        try {
            progressThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void printProgressBar(int percent) {
        int width = 50;
        int progress = percent * width / 100;
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            if (i < progress) {
                bar.append("=");
            } else if (i == progress) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("] ").append(percent).append("%");
        System.out.print("\r" + bar.toString());
    }
}