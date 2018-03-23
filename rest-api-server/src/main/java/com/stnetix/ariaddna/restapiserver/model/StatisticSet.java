/*
 * Copyright (c) 2018 stnetix.com. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.stnetix.ariaddna.restapiserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Contain set of statistic information about API object.
 */
@ApiModel(description = "Contain set of statistic information about API object.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-24T10:27:06.657+03:00")

public class StatisticSet {
    @JsonProperty("statisticSet")
    private List<Statistic> statisticSet = new ArrayList<Statistic>();

    public StatisticSet statisticSet(List<Statistic> statisticSet) {
        this.statisticSet = statisticSet;
        return this;
    }

    public StatisticSet addStatisticSetItem(Statistic statisticSetItem) {
        this.statisticSet.add(statisticSetItem);
        return this;
    }

    /**
     * Get statisticSet
     * @return statisticSet
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public List<Statistic> getStatisticSet() {
        return statisticSet;
    }

    public void setStatisticSet(List<Statistic> statisticSet) {
        this.statisticSet = statisticSet;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatisticSet statisticSet = (StatisticSet) o;
        return Objects.equals(this.statisticSet, statisticSet.statisticSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statisticSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StatisticSet {\n");

        sb.append("    statisticSet: ").append(toIndentedString(statisticSet)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

