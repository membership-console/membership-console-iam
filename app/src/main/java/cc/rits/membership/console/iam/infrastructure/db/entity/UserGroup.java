package cc.rits.membership.console.iam.infrastructure.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_group.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_group.name
     *
     * @mbg.generated
     */
    private String name;
}