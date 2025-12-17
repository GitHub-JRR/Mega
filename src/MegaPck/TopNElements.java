package MegaPck;

import java.util.*;


public class TopNElements {

	    public static List<Calibragem> getTopN(List<Calibragem> ranking, int n, int ordem) {
	        if (n <= 0 || ranking.isEmpty()) return Collections.emptyList();

	        // 1. Obtém o Comparator adequado para a ordem especificada
	        Comparator<Calibragem> comparator = getComparator(ordem);
	        
	        // 2. Cria um min-heap usando o Comparator
	        PriorityQueue<Calibragem> minHeap = new PriorityQueue<>(comparator);
	        
	        // 3. Processa todos os elementos
	        for (Calibragem item : ranking) {
	            if (minHeap.size() < n) {
	                minHeap.offer(item);
	            } else {
	                // Compara com o menor elemento atual do heap (que é o topo)
	                if (comparator.compare(item, minHeap.peek()) > 0) {
	                    minHeap.poll();
	                    minHeap.offer(item);
	                }
	            }
	        }
	        
	        // 4. Converte para lista ordenada descendente
	        List<Calibragem> result = new ArrayList<>(minHeap);
	        result.sort(comparator.reversed());
	        return result;
	    }

	    // Factory de Comparators para cada tipo de ordenação
	    private static Comparator<Calibragem> getComparator(int ordem) {
	        switch (ordem) {
	            case 0:
	                return Comparator.comparingLong(Calibragem::calcRankTotais);
	            case 1:
	                return Comparator.comparingLong(Calibragem::ordem1);
	            case 2:
	                return Comparator.comparingLong(Calibragem::ordem2);
	            case 3:
	                return Comparator.comparingLong(Calibragem::ordem3);
	            case 4:
	                return Comparator.comparingDouble(Calibragem::ordem4);
	            default:
	                throw new IllegalArgumentException("Ordem inválida: " + ordem);
	        }
	    }
	    
	    public static void showProgress(int current, int total) {
	        System.out.printf("\rProgresso: %d/%d (%.1f%%)", current, total, (current*100.0/total));
	    }
	    
	    // Versão com progresso (opcional)
	    public static List<Calibragem> getTopNWithProgress(List<Calibragem> ranking, int n, int ordem,ProgressPrinter progress) {
	        if (n <= 0 || ranking.isEmpty()) return Collections.emptyList();
	        //PriorityQueue<Calibragem> heap = new PriorityQueue<>(getComparator(ordem));
	        Comparator<Calibragem> comparator = getComparator(ordem);
	        //PriorityQueue<Calibragem> minHeap = new PriorityQueue<>(comparator);
	        //AtomicInteger progress = new AtomicInteger(0);
	        //int total = ranking.size();
	        PriorityQueue<Calibragem> minHeap = new PriorityQueue<>(getComparator(ordem));
	        // Thread de progresso
	        /*
	        new Thread(() -> {
	            int lastPercent = -1;
	            while (progress.get() < total) {
	                int percent = (progress.get() * 100) / total;
	                if (percent != lastPercent) {
	                    System.out.printf("\rProcessando: %d%%", percent);
	                    System.out.flush();
	                    lastPercent = percent;
	                }
	                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
	            }
	            System.out.println("\rConcluído! 100%");
	        }).start();
            */
	        
	        int totalItems = ranking.size();
	        ProgressTracker tracker = new ProgressTracker(totalItems);
	        tracker.print(1);
	        final int fracao = (totalItems /10)&~1;// força ser par
	        int initialSize = Math.min(n, ranking.size());
	        for (int i = 0; i < initialSize; i++) {
	            minHeap.offer(ranking.get(i));
	        }
	        
	        // Fase 2: Processa o restante (apenas comparações necessárias)
	        for (int i = initialSize; i < ranking.size(); i++) {
	            Calibragem current = ranking.get(i);
	            if (comparator.compare(current, minHeap.peek()) > 0) {
	                minHeap.poll();
	                minHeap.offer(current);
	            }
	            if (i % fracao == 0 ) {
	            	tracker.print(i);
	            }
	        }
	        
	        tracker.print(totalItems);
	        List<Calibragem> result = new ArrayList<>(minHeap);
	        result.sort(comparator.reversed());
	        return result;
	    }
	}