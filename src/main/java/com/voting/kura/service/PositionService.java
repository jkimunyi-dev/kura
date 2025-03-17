package com.voting.kura.service;

import com.voting.kura.exception.VotingException;
import com.voting.kura.model.Position;
import com.voting.kura.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    /**
     * Get a position by its ID
     *
     * @param id The ID of the position to retrieve
     * @return The position with the specified ID
     * @throws VotingException if the position is not found
     */
    public Position getPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new VotingException("Position not found with id: " + id));
    }

    /**
     * Create a new position
     *
     * @param position The position to create
     * @return The created position
     * @throws VotingException if a position with the same name already exists
     */
    @Transactional
    public Position createPosition(Position position) {
        if (positionRepository.existsByPositionName(position.getPositionName())) {
            throw new VotingException("Position already exists with name: " + position.getPositionName());
        }

        validatePositionLevel(position.getPositionLevel());
        return positionRepository.save(position);
    }

    /**
     * Update an existing position
     *
     * @param id The ID of the position to update
     * @param positionDetails The updated position details
     * @return The updated position
     */
    @Transactional
    public Position updatePosition(Long id, Position positionDetails) {
        Position position = getPositionById(id);

        // Check if new name conflicts with existing position (excluding current position)
        if (!position.getPositionName().equals(positionDetails.getPositionName()) &&
                positionRepository.existsByPositionName(positionDetails.getPositionName())) {
            throw new VotingException("Position already exists with name: " + positionDetails.getPositionName());
        }

        validatePositionLevel(positionDetails.getPositionLevel());

        position.setPositionName(positionDetails.getPositionName());
        position.setPositionLevel(positionDetails.getPositionLevel());
        position.setDescription(positionDetails.getDescription());

        return positionRepository.save(position);
    }

    /**
     * Delete a position
     *
     * @param id The ID of the position to delete
     */
    @Transactional
    public void deletePosition(Long id) {
        Position position = getPositionById(id);
        positionRepository.delete(position);
    }

    /**
     * Get all positions
     *
     * @return List of all positions
     */
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    /**
     * Get positions by level
     *
     * @param level The position level (1=Class Rep, 2=Faculty Rep, 3=University wide)
     * @return List of positions at the specified level
     */
    public List<Position> getPositionsByLevel(Integer level) {
        validatePositionLevel(level);
        return positionRepository.findByPositionLevel(level);
    }

    /**
     * Validate position level
     *
     * @param level The position level to validate
     * @throws VotingException if the level is invalid
     */
    private void validatePositionLevel(Integer level) {
        if (level == null || level < 1 || level > 3) {
            throw new VotingException("Invalid position level. Must be 1 (Class Rep), 2 (Faculty Rep), or 3 (University wide)");
        }
    }
}