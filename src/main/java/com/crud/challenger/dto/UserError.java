/**
 * 
 */
package com.crud.challenger.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class UserError implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String description;
	

}
