package com.example.demo.Domain.Service;

import com.example.demo.Config.auth.PrincipalDetails;
import com.example.demo.Domain.Dto.MusicDto;
import com.example.demo.Domain.Dto.MylistDto;
import com.example.demo.Domain.Entity.*;
import com.example.demo.Domain.Repository.MemberRepository;
import com.example.demo.Domain.Repository.MusicRepository;
import com.example.demo.Domain.Repository.MylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MylistService {

    @Autowired
    MylistRepository mylistRepository;

    @Autowired
    MusicRepository musicRepository;

    @Autowired
    MemberRepository memberRepository;

    @Transactional(rollbackFor = SQLException.class)
    public void addMylist(List<Long> musicCodes, String username) {
        for (Long music_code : musicCodes) {
            Mylist mylist = new Mylist();

            MylistId mylistId = new MylistId();
            mylistId.setUsername(username);
            mylistId.setMusicCode(music_code);

            mylist.setMylistId(mylistId);
            Music music = musicRepository.findByMusicCode(music_code);
            Member member = memberRepository.findByUsername(username);
            mylist.setMusic(music);
            mylist.setMember(member);
            System.out.println(mylist);

            mylistRepository.save(mylist);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public List<Mylist> GetMylistByUsername(String username){
        return mylistRepository.GetMylistByUsernameAsc(username);
    }

    @Transactional(rollbackFor = SQLException.class)
    public void removeMylistById(List<Long> musicCodes, String username){
        for (Long music_code : musicCodes) {

            MylistId mylistId = new MylistId();
            mylistId.setUsername(username);
            mylistId.setMusicCode(music_code);

            Optional<Mylist> mylistOptional = mylistRepository.findById(mylistId);
            System.out.println(mylistOptional);

            if (mylistOptional.isPresent()) {
                Mylist mylist = mylistOptional.get();
                mylistRepository.delete(mylist);
            }
        }
    }

}
