package ISTP.service;

import ISTP.domain.bloodDonation.BloodTypeCategories;
import ISTP.domain.bloodDonation.BloodTypeName;
import ISTP.domain.bloodDonation.accept.Accept;
import ISTP.domain.bloodDonation.accept.AcceptStatus;
import ISTP.domain.bloodDonation.request.Request;
import ISTP.domain.bloodDonation.request.RequestStatusCategories;
import ISTP.domain.bloodDonation.request.RequestStatusName;
import ISTP.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AcceptServiceTest {

    @Autowired
    RequestService requestService;

    @Autowired
    MemberService memberService;

    @Autowired
    AcceptService acceptService;

    @Test
    public void saveAcceptTest() {
        Member member = new Member("abc", "aaa");

        memberService.save(member);
        BloodTypeCategories bloodTypeCategories = new BloodTypeCategories(BloodTypeName.A_PLUS);
        RequestStatusCategories byRequestStatus = requestService.findByRequestStatus(RequestStatusName.APPLICATION);
        Request request = new Request(member, "질병", "제목","내용", LocalDateTime.now().plusDays(3),
                "1111-2222", "병원", byRequestStatus, bloodTypeCategories,
                "가족", "혈소판 헌혈", "인천");

        requestService.save(request);

        Accept accept = new Accept(member, request, AcceptStatus.수락);

        Long id = acceptService.save(accept);

        Accept findAccept = acceptService.findById(id);
        assertThat(id).isEqualTo(findAccept.getId());
    }

    @Test
    public void update_test() {
        Member member = new Member("abc", "aaa");

        memberService.save(member);
        BloodTypeCategories bloodTypeCategories = new BloodTypeCategories(BloodTypeName.A_PLUS);
        RequestStatusCategories byRequestStatus = requestService.findByRequestStatus(RequestStatusName.APPLICATION);
        Request request = new Request(member, "질병", "제목","내용", LocalDateTime.now().plusDays(3),
                "1111-2222", "병원", byRequestStatus, bloodTypeCategories,
                "가족", "혈소판 헌혈", "인천");

        requestService.save(request);

        Accept accept = new Accept(member, request, AcceptStatus.수락);

        Long id = acceptService.save(accept);
        Accept findAccept = acceptService.findById(id);
        findAccept.update_finish();
        assertThat(findAccept.getStatus() == AcceptStatus.완료);
        findAccept.update_cancel();
        assertThat(findAccept.getStatus() == AcceptStatus.취소);
        findAccept.update_request();
        assertThat(findAccept.getStatus() == AcceptStatus.수락);
    }
}
