package br.insper.prova.teste;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class TesteControllerTests {
    @InjectMocks
    private TesteController testeController;



    private MockMvc mockMvc;
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(testeController)
                .build();
    }
    @Test
    void test_TesteHelloWorld() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/teste"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}
