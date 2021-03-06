package com.imie.a2dev.teamculte.readeo.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.joda.time.DateTime;

/**
 * Class used to hold the update data (ids and last update date).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateDataElement {
    /**
     * Stores the id or ids of the associated entity.
     */
    private int[] ids;

    /**
     * Stores the last update date.
     */
    private DateTime dateUpdated;

    /**
     * UpdateDataElement constructor initializing the values with -1.
     * @param idNb The number of ids (size of ids).
     */
    public UpdateDataElement(int idNb) {
        this.ids = new int[idNb];
        this.dateUpdated = new DateTime();

        for (int i = 0; i < idNb; ++i) {
            this.ids[i] = -1;
        }
    }

    /**
     * Gets the id at specified position.
     * @param pos The position of the id to get.
     * @return The id or -1 if pos > last index.
     */
    public int getId(int pos) {
        if (pos > this.size() - 1) {
            return -1;
        }

        return this.ids[pos];
    }

    /**
     * Gets the number of ids.
     * @return The number of ids.
     */
    public int size() {
        return this.ids.length;
    }
}
