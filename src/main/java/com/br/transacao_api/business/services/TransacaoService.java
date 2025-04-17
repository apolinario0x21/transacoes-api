package com.br.transacao_api.business.services;

import com.br.transacao_api.controller.dtos.TransacaoRequestDTO;
import com.br.transacao_api.infraestructure.exceptions.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // logs
public class TransacaoService {
    private final List<TransacaoRequestDTO> listaTransacoes = new ArrayList<>();

    public void adicionarTransacoes(TransacaoRequestDTO dto) {
      log.info("Iniciado o processamento de gravar transações" + dto);

        if(dto.dataHora().isAfter(OffsetDateTime.now())) {
            log.error("Data e hora maiores que a data e hora atual");
            throw new UnprocessableEntity("Data e hora maiores que a data e hora atual");
        }

        if (dto.valor() <= 0) {
            log.error("Valor menor ou igual a zero");
            throw new UnprocessableEntity("Valor menor ou igual a zero");
        }
        log.info("Transações add com sucesso");
        listaTransacoes.add(dto);
    }

    public void limparTransacoes() {
        log.info("Iniciado o processamento para deletar transações");
        listaTransacoes.clear();
        log.info("Transações deletadas com sucesso.");
    }

    public List<TransacaoRequestDTO> buscarTransacoes(Integer intervaloBusca) {
        log.info("Iniciada a busca de transações por tempo. " + intervaloBusca);
        OffsetDateTime dataHoraIntervalo = OffsetDateTime.now().minusSeconds(intervaloBusca);

        log.info("Retorno de transações com sucesso");
        return listaTransacoes.stream()
                .filter(transacao -> transacao.dataHora().isAfter(dataHoraIntervalo))
                .toList();
    }

}
