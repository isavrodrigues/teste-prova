package br.insper.prova.cursos.controller;

import br.insper.prova.cursos.Curso;
import br.insper.prova.cursos.CursoService;
import br.insper.prova.cursos.CursoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CursoControllerTest {

    @InjectMocks
    private CursoController cursoController;

    @Mock
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_criarCursoComSucesso() throws Exception {
        Curso curso = new Curso();
        curso.setTitulo("Curso Teste");

        when(cursoService.criarCurso(any(Curso.class), eq("admin@teste.com")))
                .thenReturn(curso);

        ResponseEntity<?> response = cursoController.criarCurso(curso, "admin@teste.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(curso, response.getBody());
    }

    @Test
    void test_criarCursoUsuarioNaoEncontrado() throws Exception {
        Curso curso = new Curso();
        curso.setTitulo("Curso Teste");

        when(cursoService.criarCurso(any(Curso.class), eq("inexistente@teste.com")))
                .thenThrow(new Exception("Usuário não encontrado"));

        ResponseEntity<?> response = cursoController.criarCurso(curso, "inexistente@teste.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    void test_criarCursoSemPermissao() throws Exception {
        Curso curso = new Curso();
        curso.setTitulo("Curso Teste");

        when(cursoService.criarCurso(any(Curso.class), eq("user@teste.com")))
                .thenThrow(new Exception("Usuário sem permissão"));

        ResponseEntity<?> response = cursoController.criarCurso(curso, "user@teste.com");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Usuário sem permissão", response.getBody());
    }

    @Test
    void test_deletarCursoComSucesso() throws Exception {
        doNothing().when(cursoService).deletarCurso("1", "admin@teste.com");

        ResponseEntity<?> response = cursoController.deletarCurso("1", "admin@teste.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void test_deletarCursoUsuarioNaoEncontrado() throws Exception {
        doThrow(new Exception("Usuário não encontrado"))
                .when(cursoService).deletarCurso("1", "inexistente@teste.com");

        ResponseEntity<?> response = cursoController.deletarCurso("1", "inexistente@teste.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    void test_deletarCursoSemPermissao() throws Exception {
        doThrow(new Exception("Usuário sem permissão"))
                .when(cursoService).deletarCurso("1", "user@teste.com");

        ResponseEntity<?> response = cursoController.deletarCurso("1", "user@teste.com");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Usuário sem permissão", response.getBody());
    }

    @Test
    void test_listarCursos() {
        Curso curso1 = new Curso();
        curso1.setTitulo("Curso A");

        Curso curso2 = new Curso();
        curso2.setTitulo("Curso B");

        when(cursoService.listarCursos()).thenReturn(Arrays.asList(curso1, curso2));

        List<Curso> cursos = cursoController.listarCursos();

        assertEquals(2, cursos.size());
        assertEquals("Curso A", cursos.get(0).getTitulo());
    }
}
