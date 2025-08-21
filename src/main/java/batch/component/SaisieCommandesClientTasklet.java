package batch.component;

import batch.service.CommandeClientService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("saisieCommandesClientTasklet")
@StepScope
public class SaisieCommandesClientTasklet implements Tasklet {

    private final CommandeClientService commandeClientService;

    @Autowired
    public SaisieCommandesClientTasklet(CommandeClientService commandeClientService){
        this.commandeClientService = commandeClientService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        this.commandeClientService.commandesClient();
        return RepeatStatus.FINISHED;
    }
}
