package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {

        SpringApplication.run(JpashopApplication.class, args);
    }

    /**LAZY는 하이버네이트에서 proxy로 새롭게 객체를 다듬어서 넣어둔다.
     * json라이브러리가 proxy를 읽어서 뿌릴때 오류가 발생한다. 그것을 못 뿌리게 모듈을(gradle에 먼저 DI) 생성 한다.(API 개발 고급 -1)
     * 이 방법도 추천하지는 않는다.(entity를 무조건 노출을 안 하는게 좋다 DTO를 따로 만든다[항상 명심])
     */
    @Bean
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        //LAZY로딩을 무시하지 않고 json생성 시점에 넣는다.
        //hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,true);
        return hibernate5Module;

    }
}
