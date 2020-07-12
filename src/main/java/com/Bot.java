package com;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private LinkedList<User> users;
    private final String CHANNEL_ID = "-1001100038218";

    public Bot() {
        users = new LinkedList<User>();
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot((LongPollingBot) new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String chatId = message.getChatId().toString();
            if (message.getText().equals("/Інформація")) {
                sendInfo(message, chatId);
            } else if (message.getText().equals("/start")) {
                start(message, chatId);
            } else if (message.getText().equals("/Додати книгу")) {
                addBook(message, chatId);
            } else if (message.getText().length() > 10) {
                addDescription(message, chatId);
            } else {
                sendMsg(message, "Якщо ви хочете продати ваші книги натисніть \"/Додати книгу\"\n");
            }
        } else if (message != null && message.hasPhoto()) {
            addPhoto(message);
        } else if (message != null && message.hasPhoto() && message.hasText()) {
            if (message.getText().length() > 10 && message.getText().length() < 5000) {

            }
        }
         if (update.hasCallbackQuery()) {
            String text = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            message = update.getCallbackQuery().getMessage();
            if (text.equals("/Інформація")) {
                sendInfo(message, chatId);
            } else if (text.equals("/Додати книгу")) {
                addBook(message, chatId);
            }


        }
    }
        public void sendInfo (Message message, String chatId){
            sendInlineKeyBoardMessage(Long.valueOf(chatId), "Я можу розмістити ваші книги на каналі\n" +
                    "Для цього натисніть \"/Додати книгу\"", "/Додати книгу", "");
        }

        public void start (Message message, String chatId){
            sendInlineKeyBoardMessage(Long.valueOf(chatId), "Привіт, я - Книжковий бот ✌\uD83C\uDFFB\n" +
                    "Якщо ви хочете продати ваші книги натисніть \"/Додати книгу\"\n" +
                    "Детальну інформацію дивіться в розділі \"/Інформація\"", "/Додати книгу", "/Інформація");
        }

        public void addBook (Message message, String chatId){
            User user = new User().setChatId(chatId);
            users.addFirst(user);
            sendMsg(message, "Надішліть фото книги");
        }

        public void addDescription (Message message, String chatId){
            String chat_id = message.getChatId().toString();
            User user = User.getUserFromList((LinkedList<User>) users, chat_id);
            user.getPhoto().setCaption(message.getText());
            try {
                sendPhoto(user.getPhoto());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void addPhoto (Message message){
            long message_id = message.getMessageId();
            String chat_id = message.getChatId().toString();
            try {
                User user = User.getUserFromList((LinkedList<User>) users, chat_id);
                List<PhotoSize> photos = message.getPhoto();

                String f_id = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getFileId();

                int f_width = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getWidth();

                int f_height = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getHeight();
                SendPhoto msg = new SendPhoto()
                        .setChatId(CHANNEL_ID)
                        .setPhoto(f_id);

                user.getPhoto().setChatId(chat_id)
                        .setPhoto(f_id);
                sendMsg(message, "Надішліть назву, опис, ціну і категорії-хештеги книги");
                sendMsg(message, "Наприклад:\n" +
                        "Назва книги\n" +
                        "Опис\n" +
                        "Ціна\n" +
                        "#хештег1 #хештег2");
                sendMsg(message, "#Художні\n" +
                        "#СучасніАвтори " +
                        "#РоманиПроКохання " +
                        "#ПригодницькоІсторичніРомани " +
                        "#Детективи " +
                        "#Фантастика " +
                        "#Фентезі " +
                        "#Трилери " +
                        "#Класика " +
                        "#КнигиДляПідлітків " +
                        "#Комікси\n" +
                        "#НауковоПопулярні\n" +
                        "#Кулінарія " +
                        "#ДовідковаЛітература " +
                        "#ЗдоровяТаКраса " +
                        "#ВправніРуки " +
                        "#Психологія " +
                        "#СаморозвитокМотивація " +
                        "#БізнесЛітература " +
                        "#Езотерика " +
                        "#СвітЗахоплень " +
                        "#ДуховнаЛітература " +
                        "#ІсторіяІФакти " +
                        "#СвітТаємницьТаЗагадок " +
                        "#НавчальнаЛітература " +
                        "#Научпоп\n" +
                        "#Дитячі\n" +
                        "#ДітямДо4Років " +
                        "#ДітямДо6років " +
                        "#ДітямДо12років " +
                        "#ДітямВід12Років " +
                        "#ГотуємосяДоШколи " +
                        "#Розвиваючі книги " +
                        "#КазкиПовісті " +
                        "#Енциклопедії");
            } catch (Exception e) {
                sendMsg(message, "Якщо ви хочете продати ваші книги натисніть \"/Додати книгу\"\n");
            }
        }

        //-1001100038218
        public void sendPhoto (SendPhoto photo){
            try {
                this.execute(photo.setChatId(CHANNEL_ID));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        public void sendMsgToChannel (Message message, String text){
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(CHANNEL_ID);
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);
            try {
                final Serializable execute = execute(new SendMessage(CHANNEL_ID, text));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        public void sendMsg (Message message, String text){
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);
            try {
                final Serializable execute = execute(new SendMessage(message.getChatId(), text));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        public void sendInlineKeyBoardMessage ( long chatId, String text, String button, String button2){
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(button).setCallbackData(button);
            inlineKeyboardButton2.setText(button2).setCallbackData(button2);
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
            List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
            keyboardButtonsRow1.add(inlineKeyboardButton1);
            keyboardButtonsRow2.add(inlineKeyboardButton2);
            List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
            rowList.add(keyboardButtonsRow1);
            if (!button2.equals("")) {
                rowList.add(keyboardButtonsRow2);
            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            //return new SendMessage().setChatId(chatId).setText(text).setReplyMarkup(inlineKeyboardMarkup);
            try {
                final Serializable execute = execute(new SendMessage(chatId, text).setReplyMarkup(inlineKeyboardMarkup));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        public String getBotUsername () {
            return "bookADD_bot";
        }

        public String getBotToken () {
            return "1077351964:AAEcfNaXl1xjqQN_LPBI51SC3r_GQpjKGe0";
        }
    }
