package br.insper.prova.cursos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
class CursoService {

    private static final String USUARIO_API_URL = "http://56.124.127.89:8080/api/usuario/";

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public UsuarioResponse getUsuario(String email) throws Exception {
        try {
            ResponseEntity<UsuarioResponse> response = restTemplate.getForEntity(USUARIO_API_URL + email, UsuarioResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception("Usuário não encontrado");
        }
    }

    public Curso criarCurso(Curso curso, String email) throws Exception {
        UsuarioResponse usuario = getUsuario(email);
        if (!"ADMIN".equalsIgnoreCase(usuario.getPapel())) {
            throw new Exception("Usuário sem permissão");
        }
        curso.setEmailUsuario(usuario.getEmail());
        curso.setNomeUsuario(usuario.getNome());
        return cursoRepository.save(curso);
    }

    public void deletarCurso(String id, String email) throws Exception {
        UsuarioResponse usuario = getUsuario(email);
        if (!"ADMIN".equalsIgnoreCase(usuario.getPapel())) {
            throw new Exception("Usuário sem permissão");
        }
        cursoRepository.deleteById(id);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }
}
