package br.com.baozistore.controller;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtos;

    public ProdutoController(ProdutoRepository produtos) {
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody DadosProduto dados) {
        Produto produto = produtos.save(new Produto(dados.nome().trim(), dados.preco(), dados.estoque()));
        return ResponseEntity.created(URI.create("/produtos/" + produto.getId())).body(produto);
    }

    @GetMapping
    public List<Produto> listar() {
        return produtos.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public Produto consultar(@PathVariable Long id) {
        return produtos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtos.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }

    public record DadosProduto(@NotBlank @Size(max = 120) String nome,
                               @NotNull @Positive @Digits(integer = 10, fraction = 2) BigDecimal preco,
                               @NotNull Boolean estoque) {
    }
}
