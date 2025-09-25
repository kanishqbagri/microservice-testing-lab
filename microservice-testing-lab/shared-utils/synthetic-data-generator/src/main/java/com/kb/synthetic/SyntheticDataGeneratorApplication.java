package com.kb.synthetic;

import com.kb.synthetic.service.SyntheticDataGeneratorService;
import com.kb.synthetic.service.MLDatasetExporterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SyntheticDataGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyntheticDataGeneratorApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataGenerationRunner(
            SyntheticDataGeneratorService generatorService,
            MLDatasetExporterService exporterService) {
        return args -> {
            if (args.length > 0 && "generate".equals(args[0])) {
                int prCount = args.length > 1 ? Integer.parseInt(args[1]) : 1000;
                int runsPerPR = args.length > 2 ? Integer.parseInt(args[2]) : 5;
                String outputPath = args.length > 3 ? args[3] : "./ml-datasets";
                
                log.info("Starting synthetic data generation: {} PRs, {} runs per PR", prCount, runsPerPR);
                
                // Generate synthetic data
                generatorService.generateSyntheticData(prCount, runsPerPR);
                
                // Export ML datasets
                exporterService.exportMLDataset(outputPath);
                exporterService.exportDatasetStatistics(outputPath);
                
                log.info("Synthetic data generation and export completed successfully!");
            } else {
                log.info("Synthetic Data Generator Application started. Use 'generate [prCount] [runsPerPR] [outputPath]' to generate data.");
            }
        };
    }
}
