package br.insper.prova.cursos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USUARIO_API = "http://56.124.127.89:8080/api/usuario/";

    public Curso criarCurso(Curso curso, String email) throws Exception {
        UsuarioResponse usuario = buscarUsuario(email);

        if (!"ADMIN".equals(usuario.getPapel())) {
            throw new Exception("Usuário não tem permissão.");
        }

        curso.setEmailUsuario(usuario.getEmail());
        curso.setNomeUsuario(usuario.getNome());
        return cursoRepository.save(curso);
    }

    public void deletarCurso(String id, String email) throws Exception {
        UsuarioResponse usuario = buscarUsuario(email);

        if (!"ADMIN".equals(usuario.getPapel())) {
            throw new Exception("Usuário não tem permissão.");
        }

        cursoRepository.deleteById(id);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    private UsuarioResponse buscarUsuario(String email) throws Exception {
        ResponseEntity<UsuarioResponse> response = restTemplate.getForEntity(
                USUARIO_API + email,
                UsuarioResponse.class
        );

        UsuarioResponse usuario = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || usuario == null) {
            throw new Exception("Usuário não encontrado.");
        }

        return usuario;
    }
}