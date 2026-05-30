package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IAComunicadosService {

    private final ChatModel chatModel;

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    public IAComunicadosService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generarComunicado(String titulo, String borrador) {
        if (apiKey == null || apiKey.isBlank()) {
            return generarBasico(titulo, borrador);
        }

        try {
            SystemMessage mensajeSistema = new SystemMessage(
                    "Eres un asistente que redacta comunicados formales y claros para un sistema de gestión de condominios.");

            String plantillaUsuario = """
                    Por favor, redacta un comunicado formal basado en la siguiente información:

                    Título: {titulo}
                    Borrador/Ideas: {borrador}

                    El comunicado debe ser profesional, empático y fácil de entender para los residentes.
                    """;

            PromptTemplate plantillaPromptUsuario = new PromptTemplate(plantillaUsuario);
            UserMessage mensajeUsuario = (UserMessage) plantillaPromptUsuario.createMessage(Map.of(
                    "titulo", titulo,
                    "borrador", borrador));

            Prompt prompt = new Prompt(List.of(mensajeSistema, mensajeUsuario));

            return chatModel.call(prompt).getResult().getOutput().getContent().trim();

        } catch (Exception ex) {
            return generarBasico(titulo, borrador);
        }
    }

    private String generarBasico(String titulo, String borrador) {
        return "COMUNICADO: " + titulo.toUpperCase() + "\n\n" + borrador.trim();
    }
}
