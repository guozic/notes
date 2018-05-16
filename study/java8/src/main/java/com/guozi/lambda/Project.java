package com.guozi.lambda;

/**
 * @author guozi
 * @date 2018-05-15 15:08
 */


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {

    /**
     * 项目名称
     */
    private String  name;

    /**
     * 编程语言
     */
    private String  language;

    /**
     * star 数
     */
    private Integer stars;

    /**
     * 描述
     */
    private String  description;

    /**
     * 作者
     */
    private String  author;

}
