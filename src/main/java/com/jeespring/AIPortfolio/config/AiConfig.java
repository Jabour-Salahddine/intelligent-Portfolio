package com.jeespring.AIPortfolio.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;


@Configuration
public class AiConfig {

    private static final String API_KEY = System.getenv("exemple_api"); // Ton token GitHub PAT
    private static final String BASE_URL = "https://models.inference.ai.azure.com";
    private static final String MODEL_NAME = "gpt-4o";

    /** Modèle classique (non streaming) */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .temperature(0.2)
                .build();
    }

    /**  Modèle streaming (pour Flux<String>) */
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .temperature(0.2)
                .build();
    }

    @Bean
    ChatMemoryProvider chatMemoryProvider(){
        // on a ajouter une mémoire de 12 messages à notre agent
        return chatId -> MessageWindowChatMemory.withMaxMessages(12);
    }

    // *****************embedding for vector database and implementation of rag concept :***********************************

     // embedding model bean :
     @Bean
     public EmbeddingModel embeddingModel() {
         return OpenAiEmbeddingModel.builder()
                 .apiKey(API_KEY)
                 .baseUrl(BASE_URL)
                 .modelName("text-embedding-3-small") // ou text-embedding-3-large
                 .build();
     }

     // tokenizer bean :
     @Bean
     public OpenAiTokenizer tokenizer() {
         return new OpenAiTokenizer("gpt-4o"); // Compatible avec GPT-4o
     }


     // the embedding store bean (vector database) :

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {

       // return new InMemoryEmbeddingStore<>(); // Using an in-memory store for simplicity
        EmbeddingModel embeddingModel = embeddingModel();
        return PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5432)
                .database("agenticRagDb")
                .user("admin")
                .password("1234")
                .table("portfolio_vectors")
                .dimension(embeddingModel.dimension())
                .dropTableFirst(true) // remet la table à zéro au redémarrage (utile en dev)
                .build();


    }

    // Load a document (cv.pdf) into the vector store at application startup :
    @Bean
    ApplicationRunner loadDocumentsToVectorStore(
            ChatLanguageModel chatLanguageModel,
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            @Value("classpath:/dossier") Resource dossierRessource
    ) {
        return args -> {

            // Parser PDF
            DocumentParser parser = new ApachePdfBoxDocumentParser();

            // Configuration de l’ingestor
            var ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(1000, 100, tokenizer()))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            // Lister tous les fichiers PDF du dossier
            File dossier = dossierRessource.getFile();
            File[] fichiers = dossier.listFiles((dir, name) -> name.endsWith(".pdf"));

            if (fichiers != null) {
                for (File fichier : fichiers) {
                    System.out.println("📄 Chargement du fichier : " + fichier.getName());
                    Document document = parser.parse(new FileInputStream(fichier));
                    ingestor.ingest(document);
                }
            } else {
                System.out.println("Aucun fichier trouvé dans le dossier !");
            }
        };
    }
    @Bean
    ContentRetriever contentRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore){

        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel) // Sert à convertir la question de l’utilisateur en vecteur (embedding).
                .embeddingStore(embeddingStore) // La "base de données" vectorielle où sont stockés les embeddings des documents.
                .maxResults(2) // Nombre maximum de segments(chunks) de contenu à récupérer.
                .minScore(0.6) // Score minimum de similarité pour qu’un segment soit considéré comme pertinent.
                .build();
    }

























}