package br.insper.prova.cursos.service;
import br.insper.prova.cursos.Curso;
import br.insper.prova.cursos.CursoRepository;
import br.insper.prova.cursos.CursoService;
import br.insper.prova.cursos.UsuarioResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void test_criarCursoComAdmin() throws Exception {
        Curso curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso Teste");
        curso.setDescricao("Descrição do curso");
        curso.setCargaHoraria(40);
        curso.setInstrutor("Professor X");

        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setNome("Admin");
        usuario.setEmail("admin@teste.com");
        usuario.setPapel("ADMIN");

        Mockito.when(restTemplate.getForEntity(
                Mockito.eq("http://56.124.127.89:8080/api/usuario/admin@teste.com"),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(usuario, HttpStatus.OK));

        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(curso);

        Curso criado = cursoService.criarCurso(curso, "admin@teste.com");

        Assertions.assertNotNull(criado);
        Assertions.assertEquals("Curso Teste", criado.getTitulo());
        Assertions.assertEquals("admin@teste.com", criado.getEmailUsuario());
        Assertions.assertEquals("Admin", criado.getNomeUsuario());
    }

    @Test
    void test_criarCursoUsuarioNaoEncontrado() {
        Curso curso = new Curso();
        curso.setTitulo("Curso Teste");

        Mockito.when(restTemplate.getForEntity(
                Mockito.anyString(),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            cursoService.criarCurso(curso, "naoexiste@teste.com");
        });

        Assertions.assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void test_criarCursoComUserSemPermissao() {
        Curso curso = new Curso();
        curso.setTitulo("Curso Teste");

        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setNome("User");
        usuario.setEmail("user@teste.com");
        usuario.setPapel("USER");

        Mockito.when(restTemplate.getForEntity(
                Mockito.anyString(),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(usuario, HttpStatus.OK));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            cursoService.criarCurso(curso, "user@teste.com");
        });

        Assertions.assertEquals("Usuário não tem permissão.", exception.getMessage());
    }

    @Test
    void test_deletarCursoComPermissao() throws Exception {
        Curso curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso Teste");

        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setNome("Admin");
        usuario.setEmail("admin@teste.com");
        usuario.setPapel("ADMIN");

        Mockito.when(restTemplate.getForEntity(
                Mockito.eq("http://56.124.127.89:8080/api/usuario/admin@teste.com"),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(usuario, HttpStatus.OK));

        cursoService.deletarCurso("1", "admin@teste.com");

        Mockito.verify(cursoRepository, Mockito.times(1)).deleteById("1");
    }

    @Test
    void test_deletarCursoSemPermissao() {
        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setNome("User");
        usuario.setEmail("user@teste.com");
        usuario.setPapel("USER");

        Mockito.when(restTemplate.getForEntity(
                Mockito.eq("http://56.124.127.89:8080/api/usuario/user@teste.com"),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(usuario, HttpStatus.OK));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            cursoService.deletarCurso("1", "user@teste.com");
        });

        Assertions.assertEquals("Usuário não tem permissão.", exception.getMessage());
    }

    @Test
    void test_deletarCursoUsuarioNaoExiste() {
        Mockito.when(restTemplate.getForEntity(
                Mockito.anyString(),
                Mockito.eq(UsuarioResponse.class))
        ).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            cursoService.deletarCurso("1", "naoexiste@teste.com");
        });

        Assertions.assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void test_listarCursos() {
        Curso curso = new Curso();
        curso.setId("1");
        curso.setTitulo("Curso Teste");

        List<Curso> cursos = new ArrayList<>();
        cursos.add(curso);

        Mockito.when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarCursos();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Curso Teste", resultado.get(0).getTitulo());
    }
}