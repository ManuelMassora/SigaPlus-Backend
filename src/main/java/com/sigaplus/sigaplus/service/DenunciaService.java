package com.sigaplus.sigaplus.service;

import com.sigaplus.sigaplus.dto.ComentarioDto;
import com.sigaplus.sigaplus.dto.CriarDenuncia;
import com.sigaplus.sigaplus.dto.DenunciaDto;
import com.sigaplus.sigaplus.dto.PerfilEstudanteDto;
import com.sigaplus.sigaplus.model.Denuncia;
import com.sigaplus.sigaplus.model.Notificacao;
import com.sigaplus.sigaplus.model.Usuario;
import com.sigaplus.sigaplus.repo.ComentarioRepository;
import com.sigaplus.sigaplus.repo.DenunciaRepository;
import com.sigaplus.sigaplus.repo.NotificacaoRepository;
import com.sigaplus.sigaplus.repo.PerfilEstudanteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DenunciaService {
    private final DenunciaRepository denunciaRepository;
    private final ComentarioRepository comentarioRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final PerfilEstudanteRepository perfilEstudanteRepository;

    public DenunciaService(DenunciaRepository denunciaRepository,
                           ComentarioRepository comentarioRepository,
                           NotificacaoRepository notificacaoRepository,
                           PerfilEstudanteRepository perfilEstudanteRepository) {
        this.denunciaRepository = denunciaRepository;
        this.comentarioRepository = comentarioRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.perfilEstudanteRepository = perfilEstudanteRepository;
    }

    @Transactional
    public void denunciar(long comentarioId, CriarDenuncia dto) {
        var comentario = comentarioRepository.findByIdAndRemovidoIsFalse(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException("comentario nao econtrado"));
        Denuncia denuncia = new Denuncia();
        denuncia.setComentario(comentario);
        denuncia.setMotivo(dto.motivo());
        denunciaRepository.save(denuncia);
        String texto = String.format("recebeste uma denuncia pelo seu comentário: '%s'. no topico '%s' pelo motivo de '%s'",
                comentario.getConteudo(),comentario.getPostagem().getTopico().getTitulo(), denuncia.getMotivo());
        notificar(comentario.getUsuario(), texto);
    }

    public DenunciaDto buscar(long denunciaId) {
        var denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new EntityNotFoundException("denuncia nao econtrada"));
        return getDenunciaDto(denuncia);
    }

    public Page<DenunciaDto> listar(Pageable pageable) {
        var denuncias = denunciaRepository.findAll(pageable);
        if (denuncias.isEmpty()) {
            throw new EntityNotFoundException("nenhum comentario ainda");
        }
        return denuncias.map(this::getDenunciaDto);
    }

    private DenunciaDto getDenunciaDto(Denuncia denuncia) {
        var perfilEstudante = perfilEstudanteRepository.findByUsuarioId(denuncia.getComentario().getUsuario().getId())
                .orElseThrow(() -> new InternalServerErrorException("erro inesperado"));
        return new DenunciaDto(
                denuncia.getId(),
                denuncia.getMotivo(),
                denuncia.getDataCriacao(),
                new ComentarioDto(
                        denuncia.getComentario().getId(),
                        denuncia.getComentario().getConteudo(),
                        denuncia.getComentario().getDataCriacao(),
                        null,
                        new PerfilEstudanteDto(
                                perfilEstudante.getNome(),
                                perfilEstudante.getCurso(),
                                perfilEstudante.getAnoAcademico(),
                                perfilEstudante.getSobreMim(),
                                perfilEstudante.getEspecialidadeEm(),
                                perfilEstudante.getUrlImagem()
                        )
                )
        );
    }

    private void notificar(Usuario usuario, String texto) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTexto(texto);
        notificacaoRepository.save(notificacao);
    }
}
