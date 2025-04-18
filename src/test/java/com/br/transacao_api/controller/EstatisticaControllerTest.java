package com.br.transacao_api.controller;

import com.br.transacao_api.business.services.EstatisticasService;
import com.br.transacao_api.controller.dtos.EstatisticasResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// ajudar a fazer o teste
@ExtendWith(MockitoExtension.class) // extensão do JUnit 5 para usar o Mockito
public class EstatisticaControllerTest {

    @InjectMocks // injeta a classe que está sendo testada
    EstatisticasController estatisticasController;

    @Mock // injeta a classe que está sendo mockada
    EstatisticasService estatisticasService;

    EstatisticasResponseDTO estatisticas; // objeto de resposta que será usado nos testes

    MockMvc mockMvc; // cria um objeto MockMvc para simular requisições HTTP

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(estatisticasController).build();
        estatisticas = new EstatisticasResponseDTO(
                1L,
                20.0,
                20.0,
                20.0,
                20.0
        );

    }

    @Test
    void deveBuscarEstatisticasComSucesso() throws Exception {

        when(estatisticasService.calcularEstatisticasTransacoes(60))
                .thenReturn(estatisticas);

        mockMvc.perform(get("/estatistica")
                .param("IntervaloBusca", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(estatisticas.count()));
    }

}
