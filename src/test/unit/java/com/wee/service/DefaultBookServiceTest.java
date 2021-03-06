package com.wee.service;

import com.wee.BookUnitBaseTest;
import com.wee.exception.BookAlreadyExistException;
import com.wee.exception.BookNotFoundException;
import com.wee.mapper.BookMapper;
import com.wee.model.Book;
import com.wee.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.wee.model.Category.IT;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class DefaultBookServiceTest extends BookUnitBaseTest {

    @InjectMocks
    private DefaultBookService bookService;

    @Mock
    private BookRepository bookRepository;

    private BookMapper mapper = new BookMapper();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setField(bookService, "mapper", mapper);
    }

    @Test
    public void shouldGetActiveBooksByName() throws Exception {
        when(bookRepository.findByActiveTrueAndName(BOOK_NAME)).thenReturn(asList(givenActiveBookEntity(BOOK_ID)));

        List<Book> bookList = bookService.getBooksByName(BOOK_NAME);

        assertEquals(bookList.size(), 1);
        assertEquals(bookList.get(0).getBookId(), "123456");
        assertEquals(bookList.get(0).getName(), BOOK_NAME);
        assertEquals(bookList.get(0).getAuthor(), BOOK_AUTHOR);
        assertEquals(bookList.get(0).getYear(), BOOK_YEAR);
        assertEquals(bookList.get(0).getPublisher(), BOOK_PUBLISHER);
        assertEquals(bookList.get(0).getDescription(), BOOK_DESCRIPTION);
        assertEquals(bookList.get(0).getCategory(), IT);
        assertEquals(bookList.get(0).getImage(), BOOK_IMAGE);
        assertEquals(bookList.get(0).isActive(), true);
    }

    @Test
    public void shouldGetActiveBookById() throws Exception {
        when(bookRepository.findByActiveTrueAndBookId(BOOK_ID)).thenReturn(givenActiveBookEntity(BOOK_ID));

        Book book = bookService.getBookById("123456");

        assertEquals(book.getBookId(), "123456");
        assertEquals(book.getName(), BOOK_NAME);
        assertEquals(book.getAuthor(), BOOK_AUTHOR);
        assertEquals(book.getYear(), BOOK_YEAR);
        assertEquals(book.getPublisher(), BOOK_PUBLISHER);
        assertEquals(book.getDescription(), BOOK_DESCRIPTION);
        assertEquals(book.getCategory(), IT);
        assertEquals(book.getImage(), BOOK_IMAGE);
        assertEquals(book.isActive(), true);
    }

    @Test(expected = BookNotFoundException.class)
    public void shouldNotGetInActiveBookById() throws Exception {
        when(bookRepository.findByActiveTrueAndBookId(BOOK_ID)).thenReturn(null);

        bookService.getBookById("123456");
    }

    @Test(expected = BookAlreadyExistException.class)
    public void shouldNotCreateDuplicateBook() throws Exception {
        when(bookRepository.findByActiveTrueAndName(BOOK_NAME))
                .thenReturn(asList(givenActiveBookEntity(BOOK_ID)));

        bookService.createBook(givenActiveBook());
    }
}