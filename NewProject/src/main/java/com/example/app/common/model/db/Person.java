package com.example.app.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter // 1
@Setter // 2
@Entity // 3
public class Person {
	
	 @Id // 4
	 @GeneratedValue // 5
	 private Long id;

	 @Column // 6
	 private String name;

	 @Column
	 private String password;
}
/*
 * ① Lombok
 *   -  getter 메소드 만든다
 *   - getId, getName, getPassword 이런식으로 하나하나 생성해줘한다.
 * ② Lombok
 *   - setter 메소드 만든다
 *   - 사용하지 않을 시 하나하나 생성해줘야한다. 
 * ③ JPA : 엔티티 클래스임을 정의하기 위해 사용한다.
 * ④ JPA : PK(Primary key)에 매핑하기 위해 사용한다.
 * ⑤ JPA, PK키의 자동 생성전략을 명시한다.
 * ⑥ JPA, 필드를 컬럼에 매핑하기 위해 사용한다.
 */