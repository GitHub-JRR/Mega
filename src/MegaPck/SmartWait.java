package MegaPck;

import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SmartWait {
    private GerenciadorMensagens gm;
    private List<TimeInterval> intervals;
    private Duration duracao = Duration.ZERO ;
   
    
    public SmartWait(GerenciadorMensagens gm) {
        this.gm = gm;
        
        this.intervals = new ArrayList<>();
    }
     
    public Duration getDuracao() {
    	return duracao;
    }
    /**
     * Enum para representar as unidades de tempo
     */
    public enum TimeUnitMeasure {
        HOURS("horas", 3600000),
        MINUTES("minutos", 60000),
        SECONDS("segundos", 1000),
        MILLISECONDS("milissegundos", 1);
        
        private final String description;
        private final long milliseconds;
        
        TimeUnitMeasure(String description, long milliseconds) {
            this.description = description;
            this.milliseconds = milliseconds;
        }
        
        public long toMilliseconds(long value) {
            return value * milliseconds;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Adiciona um intervalo de tempo com sua respectiva duração de wait
     * @param startTime Horário de início do intervalo (formato HH:MM)
     * @param waitValue Valor da duração do wait
     * @param unit Unidade de medida (HOURS, MINUTES, SECONDS, MILLISECONDS)
     */
    public void addInterval(String startTime, long waitValue, TimeUnitMeasure unit) {
        LocalTime time = LocalTime.parse(startTime);
        long waitMilliseconds = unit.toMilliseconds(waitValue);
        intervals.add(new TimeInterval(time, waitValue, unit, waitMilliseconds));
        // Ordena os intervalos por horário de início
        intervals.sort((a, b) -> a.startTime.compareTo(b.startTime));
    }
    
    /**
     * Sobrecarga do método para manter compatibilidade com minutos
     */
    public void addInterval(String startTime, int waitMinutes) {
        addInterval(startTime, waitMinutes, TimeUnitMeasure.MINUTES);
    }
    
    /**
     * Método principal que realiza o wait inteligente baseado no relógio do sistema
     * Controla o tempo em função do relógio do computador, resistente a hibernação
     */
    public void smartWait() throws InterruptedException {
        if (intervals.isEmpty()) {
            throw new IllegalStateException("Nenhum intervalo configurado");
        }
        
        // Usa LocalDateTime para considerar data completa
        LocalDateTime startWaitDateTime = LocalDateTime.now();
        LocalTime     startWaitTime = startWaitDateTime.toLocalTime();
        
        TimeInterval initialInterval = findCurrentInterval(startWaitTime);
        
        if (initialInterval == null) {
            // Fora de todos os intervalos, usa o último intervalo
            initialInterval = intervals.get(intervals.size() - 1);
        } 
        duracao=duracao.plusMillis( initialInterval.waitMilliseconds);
        LocalDateTime targetDateTime = calculateTargetDateTime(startWaitDateTime, initialInterval);
        
       // gm.registrarMensagem("Iniciando wait às " + startWaitDateTime + 
       //                   " até " + targetDateTime + 
       //                   " (" + initialInterval.waitValue + " " + 
       //                   initialInterval.unit.getDescription() + ")");
        
        performClockBasedWait(targetDateTime);
    }
    
    /**
     * Calcula o horário alvo considerando data completa
     */
    private LocalDateTime calculateTargetDateTime(LocalDateTime currentDateTime, TimeInterval interval) {
        LocalDateTime targetDateTime = currentDateTime.plus(interval.waitMilliseconds, ChronoUnit.MILLIS);
        
        // Verifica se o target passa para outro intervalo
        LocalTime targetTime = targetDateTime.toLocalTime();
        TimeInterval targetInterval = findCurrentInterval(targetTime);
        
        // Se mudou de intervalo, pode precisar de ajuste
        if (targetInterval != interval) {
            // Encontra o início do próximo intervalo
            LocalTime nextIntervalStart = findNextIntervalStart(interval.startTime);
            
            if (nextIntervalStart != null) {
                // Calcula quando o próximo intervalo começaria
                LocalDateTime nextIntervalDateTime = currentDateTime.toLocalDate().atTime(nextIntervalStart);
                
                // Se o currentDateTime for depois do nextIntervalStart no mesmo dia, 
                // considera o próximo dia
                if (currentDateTime.toLocalTime().isAfter(nextIntervalStart)) {
                    nextIntervalDateTime = nextIntervalDateTime.plusDays(1);
                }
                
                // Calcula o fim do próximo intervalo
                LocalDateTime nextIntervalEnd = nextIntervalDateTime.plus(targetInterval.waitMilliseconds, ChronoUnit.MILLIS);
                
                // Verifica se o target original é depois do fim do próximo intervalo
                if (targetDateTime.isAfter(nextIntervalEnd)) {
                    // Se sim, mantém o target original
                    return targetDateTime;
                } else {
                    // Se não, usa o fim do próximo intervalo
                    return nextIntervalEnd;
                }
            }
        }
        
        return targetDateTime;
    }
    
    /**
     * Encontra o início do próximo intervalo após o horário especificado
     */
    private LocalTime findNextIntervalStart(LocalTime afterTime) {
        if (intervals.isEmpty()) {
            return null;
        }
        
        for (TimeInterval interval : intervals) {
            if (interval.startTime.isAfter(afterTime)) {
                return interval.startTime;
            }
        }
        
        // Se não encontrou, retorna o primeiro intervalo do próximo ciclo
        return intervals.get(0).startTime;
    }
    
    /**
     * Wait baseado no relógio do sistema - resistente a hibernação
     * Agora usando LocalDateTime para considerar data completa
     */
    private void performClockBasedWait(LocalDateTime targetDateTime) throws InterruptedException {
        LocalDateTime originalStartDateTime = LocalDateTime.now();
        LocalTime originalStartTime = originalStartDateTime.toLocalTime();
        TimeInterval lastInterval = findCurrentInterval(originalStartTime);
        
        gm.registrarMensagem("Iniciando wait às " + originalStartDateTime + " até " + targetDateTime + 
                         " (Intervalo: " + (lastInterval != null ? lastInterval.toString() : "Nenhum") + ")");
        
        while (true) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalTime currentTime = currentDateTime.toLocalTime();
            GerenciadorMensagens.verificaHibernacaoSetRegTempo();
            // Verifica se atingiu o horário alvo (considerando data)
            if (!currentDateTime.isBefore(targetDateTime)) {
                //gm.registrarMensagem("Wait concluído às " + currentDateTime);
                break;
            }
            
            // Verifica se mudou para um novo intervalo (baseado apenas no horário)
            TimeInterval currentInterval = findCurrentInterval(currentTime);
            
            // Se mudou de intervalo, recalcula o target
            if (currentInterval != null && !currentInterval.equals(lastInterval)) {
                LocalDateTime newTargetDateTime = calculateTargetDateTime(currentDateTime, currentInterval);
                
                // Só atualiza se o novo target for diferente e não tiver passado
                if (!newTargetDateTime.equals(targetDateTime) && currentDateTime.isBefore(newTargetDateTime)) {
                    targetDateTime = newTargetDateTime;
                    lastInterval = currentInterval;
                    gm.registrarMensagem("Mudança de intervalo detectada. Novo target: " + targetDateTime + 
                                     " (Intervalo: " + currentInterval.toString() + ")");
                }
            }
            
            // Calcula o tempo restante até o target
            long remainingMillis = ChronoUnit.MILLIS.between(currentDateTime, targetDateTime);
            
            if (remainingMillis <= 0) {
                break;
            }
            
            // Sleep em pequenos incrementos para verificar mudanças
            long sleepTime = Math.min(remainingMillis, 1000);
            
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
				GerenciadorMensagens.verificaHibernacaoSetRegTempo();
        }
            }
			
    }
    
    /**
     * Método alternativo que recebe horário de início e valor de intervalo com unidade
     * @param startTimeString Horário de início (formato HH:MM)
     * @param waitValue Valor da duração do wait
     * @param unit Unidade de medida (HOURS, MINUTES, SECONDS, MILLISECONDS)
     */
    public void timedWait(String startTimeString, long waitValue, TimeUnitMeasure unit) throws InterruptedException {
        LocalTime scheduleTime = LocalTime.parse(startTimeString);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();
        
        LocalDateTime scheduleDateTime = currentDateTime.toLocalDate().atTime(scheduleTime);
        
        // Se o horário agendado já passou hoje, calcula para o próximo dia
        if (currentTime.isAfter(scheduleTime)) {
            scheduleDateTime = scheduleDateTime.plusDays(1);
            gm.registrarMensagem("Horário alvo já passou, agendando para amanhã: " + scheduleDateTime);
        }
        
        // Calcula o tempo final (horário agendado + wait)
        LocalDateTime targetDateTime = scheduleDateTime.plus(unit.toMilliseconds(waitValue), ChronoUnit.MILLIS);
        
        gm.registrarMensagem("Wait programado: início às " + currentDateTime + 
                          ", agendado para " + scheduleDateTime + 
                          " + " + waitValue + " " + unit.getDescription() + 
                          " = target " + targetDateTime);
        
        // Primeiro espera até o horário agendado
        waitUntilDateTime(scheduleDateTime);
        
        // Depois espera o período adicional
        gm.registrarMensagem("Horário agendado atingido. Iniciando wait adicional de " + 
                          waitValue + " " + unit.getDescription());
        performClockBasedWait(targetDateTime);
    }
    
    /**
     * Espera até um horário específico do relógio do sistema (considerando data)
     */
    private void waitUntilDateTime(LocalDateTime targetDateTime) throws InterruptedException {
        while (true) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            
            if (!currentDateTime.isBefore(targetDateTime)) {
                break;
            }
            
            long remainingMillis = ChronoUnit.MILLIS.between(currentDateTime, targetDateTime);
            long sleepTime = Math.min(remainingMillis, 1000);
            
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
			GerenciadorMensagens.verificaHibernacaoSetRegTempo();
        }
    }
    
    /**
     * Sobrecarga do método timedWait para manter compatibilidade com minutos
     */
    public void timedWait(String startTimeString, int waitMinutes) throws InterruptedException {
        timedWait(startTimeString, waitMinutes, TimeUnitMeasure.MINUTES);
    }
    
    /**
     * Encontra o intervalo atual baseado no horário fornecido
     */
    private TimeInterval findCurrentInterval(LocalTime currentTime) {
        if (intervals.isEmpty()) {
            return null;
        }
        
        TimeInterval currentInterval = null;
        
        // Percorre todos os intervalos para encontrar o mais apropriado
        for (int i = 0; i < intervals.size(); i++) {
            TimeInterval interval = intervals.get(i);
            
            // Se o horário atual é maior ou igual ao início deste intervalo
            if (!currentTime.isBefore(interval.startTime)) {
                currentInterval = interval;
            } else {
                // Chegamos a um intervalo que começa depois do horário atual
                // Para a busca, pois os intervalos estão ordenados
                break;
            }
        }
        
        // Se não encontrou nenhum intervalo válido, usa o último (ciclo completo)
        if (currentInterval == null) {
            currentInterval = intervals.get(intervals.size() - 1);
        }
        
        return currentInterval;
    }
    
    /**
     * Lista todos os intervalos configurados
     */
    public void listIntervals() {
        if (intervals.isEmpty()) {
            System.out.println("Nenhum intervalo configurado");
            return;
        }
        
        gm.registrarMensagem("Intervalos configurados:");
        for (int i = 0; i < intervals.size(); i++) {
            TimeInterval interval = intervals.get(i);
            gm.registrarMensagem(String.format("%d. %s -> %d %s", 
                i + 1, interval.startTime, interval.waitValue, interval.unit.getDescription()));
        }
    }
    
    /**
     * Limpa todos os intervalos configurados
     */
    public void clearIntervals() {
        intervals.clear();
        gm.registrarMensagem("Todos os intervalos foram removidos");
    }
    
    /**
     * Classe interna para representar um intervalo de tempo
     */
    private static class TimeInterval {
        LocalTime startTime;
        long waitValue;
        TimeUnitMeasure unit;
        long waitMilliseconds;
        
        TimeInterval(LocalTime startTime, long waitValue, TimeUnitMeasure unit, long waitMilliseconds) {
            this.startTime = startTime;
            this.waitValue = waitValue;
            this.unit = unit;
            this.waitMilliseconds = waitMilliseconds;
        }
        
        @Override
        public String toString() {
            return startTime + " -> " + waitValue + " " + unit.getDescription();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TimeInterval that = (TimeInterval) obj;
            return startTime.equals(that.startTime) && 
                   waitValue == that.waitValue && 
                   unit == that.unit;
        }
    }
}