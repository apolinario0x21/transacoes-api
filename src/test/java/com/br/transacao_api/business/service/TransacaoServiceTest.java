package com.br.transacao_api.business.service;

import com.br.transacao_api.business.services.TransacaoService;
import com.br.transacao_api.controller.dtos.EstatisticasResponseDTO;
import com.br.transacao_api.controller.dtos.TransacaoRequestDTO;
import com.br.transacao_api.infraestructure.exceptions.UnprocessableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    TransacaoService transacaoService;
    TransacaoRequestDTO transacao;
    EstatisticasResponseDTO estatisticas;

    @BeforeEach
    void setUp() {
        transacao = new TransacaoRequestDTO(20.0, OffsetDateTime.now());
        estatisticas = new EstatisticasResponseDTO(1l, 20.0, 20.0, 20.0, 20.0);
    }

    @Test
    void DeveAdicionarTransacaoComSucesso() {

        transacaoService.adicionarTransacoes(transacao);
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.contains(transacao));
    }

    @Test
    void deveLancarExcecaoCasoValorSejaNegativo() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTransacoes(new TransacaoRequestDTO(-20.0, OffsetDateTime.now())));
        assertEquals("Valor menor ou igual a zero", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoCasoDataOuHoraMaiorQueAtual() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTransacoes(new TransacaoRequestDTO(-20.0, OffsetDateTime.now().plusDays(1))));
        assertEquals("Data e hora maiores que a data e hora atual", exception.getMessage());
    }

    @Test
    void deveLimparTransacaoComSucesso() {

        transacaoService.limparTransacoes();
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.isEmpty());
    }

    @Test
    void deveBuscarTransacaoDentroDoIntervalo() {

        TransacaoRequestDTO dto = new TransacaoRequestDTO(30.0, OffsetDateTime.now().minusHours(1));
        transacaoService.adicionarTransacoes(dto);

        transacaoService.adicionarTransacoes(transacao);

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(60);

        assertTrue(transacoes.contains(transacao));
        assertFalse(transacoes.contains(dto));
    }
}
