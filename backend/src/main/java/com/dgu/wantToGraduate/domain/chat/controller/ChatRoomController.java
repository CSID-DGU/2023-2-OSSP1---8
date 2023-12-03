package com.dgu.wantToGraduate.domain.chat.controller;

import com.dgu.wantToGraduate.domain.chat.model.ChatRoom;
import com.dgu.wantToGraduate.domain.chat.repository.ChatRoomRepository;
import com.dgu.wantToGraduate.domain.chat.service.ChatService;
import com.dgu.wantToGraduate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @GetMapping("/main")
    public String start() {
        chatService.setTestUser();
        chatService.createTestChatRoom();
        return "chat/start";
    }

    @GetMapping("/main/{userId}")
    public ResponseEntity<Map<String, String>> enterChatRoom(@PathVariable String userId) {
        log.info(userId);
        String roomId = chatService.findRoomIdByUserId(Long.parseLong(userId));
        String nickName = userRepository.findById(Long.parseLong(userId)).get().getNickname();
        Map<String, String> result = new HashMap<>();
        result.put("roomId", roomId);
        result.put("nickName", nickName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(@PathVariable String roomId) {
        return "chat/roomdetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
