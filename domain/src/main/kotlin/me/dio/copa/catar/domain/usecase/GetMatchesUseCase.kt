package me.dio.copa.catar.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

// tenho que ir no repository (onde vai ter a interface das partidas), retornar a lista de matches...

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchesRepository
) {

    // é uma função que indica que o elemento vai ser executado de maneira assíncrona, forma de dizer que ela necessita de um tratamento especial por ter essa caracteristica
    suspend operator fun invoke(): Flow<List<Match>> {
        return repository.getMatches()
    }
}