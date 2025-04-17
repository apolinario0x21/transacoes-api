package com.br.transacao_api.business.services;

import com.br.transacao_api.controller.dtos.EstatisticasResponseDTO;
import com.br.transacao_api.controller.dtos.TransacaoRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticasService {

    public final TransacaoService transacaoService;

    public EstatisticasResponseDTO calcularEstatisticasTransacoes(Integer intervaloBusca) {
        log.info("Iniciado o processamento de calcular estatísticas de transações pelo período de tempo. " + intervaloBusca);

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(intervaloBusca);

        if(transacoes.isEmpty()) {
            return new EstatisticasResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics estatisticasTransacoes = transacoes.stream()
                .mapToDouble(TransacaoRequestDTO::valor)
                .summaryStatistics();

        log.info("Estatísticas calculadas com sucesso");
        return new EstatisticasResponseDTO(estatisticasTransacoes.getCount(), estatisticasTransacoes.getSum(),
                estatisticasTransacoes.getAverage(), estatisticasTransacoes.getMin(), estatisticasTransacoes.getMax());
    }

}
