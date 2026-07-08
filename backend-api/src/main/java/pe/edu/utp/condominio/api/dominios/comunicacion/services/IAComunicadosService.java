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

                    REGLAS OBLIGATORIAS DE FORMATO:
                    - Devuelve ÚNICAMENTE el texto final del comunicado en texto plano.
                    - NO incluyas el título en el texto generado (el sistema ya lo maneja por separado).
                    - NO uses ningún tipo de formato Markdown (prohibido usar asteriscos **, guiones ---, o numerales #).
                    - NO uses frases introductorias ni de despedida conversacionales (ej. "Aquí tienes", "Claro").
                    - El comunicado debe ser profesional, empático y directo.
                    - Si falta información como fechas exactas o nombres, evítalos para que el texto sea publicable inmediatamente sin que el usuario tenga que rellenar corchetes [ ].
                    """;

            PromptTemplate plantillaPromptUsuario = new PromptTemplate(plantillaUsuario);
            UserMessage mensajeUsuario = (UserMessage) plantillaPromptUsuario.createMessage(Map.of(
                    "titulo", titulo,
                    "borrador", borrador));

            Prompt prompt = new Prompt(List.of(mensajeSistema, mensajeUsuario));

            return chatModel.call(prompt).getResult().getOutput().getContent().trim();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR IA: " + ex.getMessage() + "\n\n" + generarBasico(titulo, borrador);
        }
    }

    private String generarBasico(String titulo, String borrador) {
        return "COMUNICADO: " + titulo.toUpperCase() + "\n\n" + borrador.trim();
    }
}
