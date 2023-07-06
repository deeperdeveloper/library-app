package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository, UserLoanHistoryRepository userLoanHistoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    /**
     * 대출 내역 추가
     * 1. bookRepository에 저장된 book 객체를 불러온 후, 해당 책에 대한 대출 내역이 있는지 조회
     * 2. 대출 내역이 없다면, DB의 USER 정보를 토대로 해당 책에 대한 대출 내역 추가
     */
    //책이 DB에 있는지 없는지 우선 확인 후, DB에 반납할 것이다.
    @Transactional
    public void loanBook(BookLoanRequest request) {
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) {
            throw new IllegalArgumentException("진작 대출되어 있는 책입니다.");
        }

        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        user.loanBook(request.getBookName()); //user 객체 내에서, 대출 기능까지 처리한다.
    }

    /**
     * 반납 처리
     * 1. 요청 정보로부터, db의 유저 정보를 찾는다.
     * 2. 찾은 db의 유저 정보로부터, 책 대출 내역을 찾는다
     * 3. 반납 처리 실행
     */
    @Transactional
    public void returnBook(BookReturnRequest request) {
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        user.returnBook(request.getBookName());
    }
}