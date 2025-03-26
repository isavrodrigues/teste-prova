package br.insper.prova.cursos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {


    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<?> criarCurso(@RequestBody Curso curso, @RequestHeader(name = "email") String email) {
        try {
            return ResponseEntity.ok(cursoService.criarCurso(curso, email));
        } catch (Exception e) {
            if (e.getMessage().equals("Usuário não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCurso(@PathVariable String id, @RequestHeader(name = "email") String email) {
        try {
            cursoService.deletarCurso(id, email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            if (e.getMessage().equals("Usuário não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        }
    }

    @GetMapping
    public List<Curso> listarCursos() {
        return cursoService.listarCursos();
    }
}