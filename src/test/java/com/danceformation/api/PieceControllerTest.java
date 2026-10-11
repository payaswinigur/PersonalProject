package com.danceformation.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * End-to-end tests for the routine endpoints.
 *
 * <p>
 * These run the real controller, service and repository together rather than
 * mocking the service, so they also cover the JSON shapes and the status codes
 * chosen for each refusal. Each test creates its own routine, so they stay
 * independent despite sharing the in-memory repository bean.
 * </p>
 */
@SpringBootTest
class PieceControllerTest {

    /** The loaded application, used to build MockMvc. */
    @Autowired
    private WebApplicationContext context;

    /** Drives requests without starting a real server. */
    private MockMvc mvc;

    /**
     * Builds MockMvc over the real application context before each test.
     */
    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * Creates a routine and returns its id.
     *
     * @param width
     *            stage columns
     * @param height
     *            stage rows
     * @return the new routine's id
     * @throws Exception
     *             if the request fails
     */
    private String newPiece(int width, int height) throws Exception {
        String location = this.mvc
                .perform(post("/api/pieces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"width\":" + width + ",\"height\":"
                                + height + "}"))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        // Location is /api/pieces/{id}, so the id is its last segment
        return location.substring(location.lastIndexOf('/') + 1);
    }

    /**
     * Places a dancer, returning the raw result for further assertions.
     *
     * @param id
     *            routine id
     * @param index
     *            slide index
     * @param name
     *            dancer name
     * @param x
     *            column
     * @param y
     *            row
     * @return the performed request's result actions
     * @throws Exception
     *             if the request fails
     */
    private org.springframework.test.web.servlet.ResultActions place(String id,
            int index, String name, int x, int y) throws Exception {
        return this.mvc.perform(
                post("/api/pieces/" + id + "/formations/" + index + "/dancers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\",\"x\":" + x
                                + ",\"y\":" + y + "}"));
    }

    /**
     * A new routine starts with exactly one empty formation.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void createsRoutineWithOneEmptyFormation() throws Exception {
        this.mvc.perform(post("/api/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"width\":5,\"height\":4}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.width").value(5))
                .andExpect(jsonPath("$.height").value(4))
                .andExpect(jsonPath("$.formations.length()").value(1))
                .andExpect(jsonPath("$.formations[0].index").value(0))
                .andExpect(jsonPath("$.formations[0].dancers.length()")
                        .value(0));
    }

    /**
     * A stage must have positive dimensions, since the component's
     * representation invariant requires it.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void rejectsNonPositiveStageSize() throws Exception {
        this.mvc.perform(post("/api/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"width\":0,\"height\":4}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_stage_size"));
    }

    /**
     * A missing dimension is reported rather than defaulted.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void rejectsMissingDimension() throws Exception {
        this.mvc.perform(post("/api/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"height\":4}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("missing_field"));
    }

    /**
     * An unknown id is a 404, not an empty routine.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void readsUnknownRoutineAsNotFound() throws Exception {
        this.mvc.perform(get("/api/pieces/does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not_found"));
    }

    /**
     * A placed dancer appears in the returned state.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void placesDancer() throws Exception {
        String id = this.newPiece(5, 5);

        this.place(id, 0, "Alice", 1, 1).andExpect(status().isOk())
                .andExpect(jsonPath("$.formations[0].dancers.length()")
                        .value(1))
                .andExpect(jsonPath("$.formations[0].dancers[0].name")
                        .value("Alice"))
                .andExpect(jsonPath("$.formations[0].dancers[0].x").value(1))
                .andExpect(jsonPath("$.formations[0].dancers[0].y").value(1));
    }

    /**
     * An occupied spot is a 409 naming the occupant, rather than a silent
     * no-op the way the component alone would do it.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void refusesOccupiedPositionWithConflict() throws Exception {
        String id = this.newPiece(5, 5);
        this.place(id, 0, "Alice", 1, 1).andExpect(status().isOk());

        this.place(id, 0, "Bob", 1, 1).andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("position_occupied"))
                .andExpect(jsonPath("$.occupant").value("Alice"))
                .andExpect(jsonPath("$.x").value(1))
                .andExpect(jsonPath("$.y").value(1));

        // Alice must still hold the spot and Bob must not be on stage
        this.mvc.perform(get("/api/pieces/" + id))
                .andExpect(jsonPath("$.formations[0].dancers.length()")
                        .value(1))
                .andExpect(jsonPath("$.formations[0].dancers[0].name")
                        .value("Alice"));
    }

    /**
     * A position off the stage is the client's error.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void rejectsOffStagePosition() throws Exception {
        String id = this.newPiece(5, 5);

        this.place(id, 0, "Alice", 9, 9).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("off_stage"));
    }

    /**
     * An unknown slide index is a 404.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void rejectsUnknownFormationIndex() throws Exception {
        String id = this.newPiece(5, 5);

        this.place(id, 7, "Alice", 0, 0).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not_found"));
    }

    /**
     * Placing a dancer who is already on stage moves them instead of
     * duplicating them.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void movesDancerAlreadyOnStage() throws Exception {
        String id = this.newPiece(5, 5);
        this.place(id, 0, "Alice", 1, 1).andExpect(status().isOk());

        this.place(id, 0, "Alice", 3, 3).andExpect(status().isOk())
                .andExpect(jsonPath("$.formations[0].dancers.length()")
                        .value(1))
                .andExpect(jsonPath("$.formations[0].dancers[0].x").value(3))
                .andExpect(jsonPath("$.formations[0].dancers[0].y").value(3));
    }

    /**
     * A new formation copies the previous pose forward.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void addFormationCopiesThePreviousPose() throws Exception {
        String id = this.newPiece(5, 5);
        this.place(id, 0, "Alice", 1, 1).andExpect(status().isOk());

        this.mvc.perform(post("/api/pieces/" + id + "/formations"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.formations.length()").value(2))
                .andExpect(jsonPath("$.formations[1].dancers[0].name")
                        .value("Alice"))
                .andExpect(jsonPath("$.formations[1].dancers[0].x").value(1));
    }

    /**
     * Editing one slide must not affect another.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void editingOneFormationLeavesTheOtherAlone() throws Exception {
        String id = this.newPiece(5, 5);
        this.place(id, 0, "Alice", 1, 1).andExpect(status().isOk());
        this.mvc.perform(post("/api/pieces/" + id + "/formations"))
                .andExpect(status().isCreated());

        // Vacate the copied spot in slide 1, then put Bob there
        this.mvc.perform(delete("/api/pieces/" + id
                + "/formations/1/dancers").param("x", "1").param("y", "1"))
                .andExpect(status().isOk());
        this.place(id, 1, "Bob", 1, 1).andExpect(status().isOk())
                .andExpect(jsonPath("$.formations[0].dancers[0].name")
                        .value("Alice"))
                .andExpect(jsonPath("$.formations[1].dancers[0].name")
                        .value("Bob"));
    }

    /**
     * The last formation cannot be removed, and says so instead of silently
     * doing nothing.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void refusesRemovingTheLastFormation() throws Exception {
        String id = this.newPiece(5, 5);

        this.mvc.perform(delete("/api/pieces/" + id + "/formations/0"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("last_formation"));

        this.mvc.perform(get("/api/pieces/" + id))
                .andExpect(jsonPath("$.formations.length()").value(1));
    }

    /**
     * A formation can be removed once another exists.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void removesFormationWhenMoreThanOneExists() throws Exception {
        String id = this.newPiece(5, 5);
        this.mvc.perform(post("/api/pieces/" + id + "/formations"))
                .andExpect(status().isCreated());

        this.mvc.perform(delete("/api/pieces/" + id + "/formations/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formations.length()").value(1));
    }

    /**
     * Clearing a spot removes the dancer, and repeating it is harmless.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void removesDancerAndIsIdempotent() throws Exception {
        String id = this.newPiece(5, 5);
        this.place(id, 0, "Alice", 2, 2).andExpect(status().isOk());

        this.mvc.perform(delete("/api/pieces/" + id
                + "/formations/0/dancers").param("x", "2").param("y", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formations[0].dancers.length()")
                        .value(0));

        // Repeating the deletion must not become an error
        this.mvc.perform(delete("/api/pieces/" + id
                + "/formations/0/dancers").param("x", "2").param("y", "2"))
                .andExpect(status().isOk());
    }

    /**
     * A routine can be deleted and is then gone.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void deletesRoutine() throws Exception {
        String id = this.newPiece(3, 3);

        this.mvc.perform(delete("/api/pieces/" + id))
                .andExpect(status().isNoContent());
        this.mvc.perform(get("/api/pieces/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * The listing includes a routine that was just created.
     *
     * @throws Exception
     *             if the request fails
     */
    @Test
    void listsRoutineIds() throws Exception {
        String id = this.newPiece(3, 3);

        this.mvc.perform(get("/api/pieces")).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasItem(id)));
    }
}
