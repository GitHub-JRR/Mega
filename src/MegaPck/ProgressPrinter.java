package MegaPck;

import java.io.PrintStream;

public class ProgressPrinter {
    private static final PrintStream CONSOLE = System.out;
    private int totalSteps;
    private int currentStep = 0;
    
    public ProgressPrinter(int totalSteps) {
        this.totalSteps = totalSteps;
    }
    
    public void update() {
        currentStep++;
        printProgress();
    }
    
    private void printProgress() {
        // Salva o stream atual
        PrintStream originalOut = System.out;
        
        try {
            // Força a saída para o console
            System.setOut(CONSOLE);
            
            int percent = (currentStep * 100) / totalSteps;
            System.out.printf("\rProcessando: %d%%", percent);
            System.out.flush();
        } finally {
            // Restaura o stream original
            System.setOut(originalOut);
        }
    }
    
    public void complete() {
        PrintStream originalOut = System.out;
        try {
            System.setOut(CONSOLE);
            System.out.println("\nConcluído! 100%");
        } finally {
            System.setOut(originalOut);
        }
    }
}