-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 14, 2023 lúc 06:28 PM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `datafigure`
--
CREATE DATABASE IF NOT EXISTS `datafigure` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `datafigure`;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id` int(11) NOT NULL,
  `iddonhang` int(11) NOT NULL,
  `idsp` int(11) NOT NULL,
  `soluong` int(11) NOT NULL,
  `gia` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `diachi` text NOT NULL,
  `sodienthoai` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `soluong` int(11) NOT NULL,
  `tongtien` varchar(255) NOT NULL,
  `trangthai` int(11) NOT NULL DEFAULT 0,
  `zalo` text NOT NULL,
  `ngaydat` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` int(11) NOT NULL,
  `tensanpham` varchar(100) NOT NULL,
  `hinhanh` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`id`, `tensanpham`, `hinhanh`) VALUES
(1, 'Home', 'https://th.bing.com/th/id/OIP.2XfYRYnxCMu8gFsTcPrrjgHaHb?pid=ImgDet&rs=1'),
(2, 'Nendoroid\r\n', 'https://product.hstatic.net/1000160337/product/nendoroid_barbie__1__136f4513ead0450389a44478a970741f_compact.jpg'),
(3, 'PopUpParade\r\n', 'https://product.hstatic.net/1000160337/product/86_-eighty_six-_lena_1__8__295dbf089b614f4f8b0beb5db6313619_compact.jpg'),
(4, 'Figma', 'https://product.hstatic.net/1000160337/product/dc69e41534f7453e34021d3f57c8c89c_9f1cbf8e5ee441419219d23e6984319a_compact.jpg'),
(5, 'Liên hệ ', 'https://product.hstatic.net/1000160337/product/nendoroid_peanuts_snoopy__1__fc28a947e4784865a165ab3d3d42a62a_compact.jpg'),
(6, 'Thông tin', 'https://symbols.vn/wp-content/uploads/2022/02/Hinh-Nen-Cho-Chibi-Cho-Dien-Thoai-ngo-nghinh.jpg'),
(7, 'Đơn Hàng', 'https://newstargroup.com.vn/wp-content/uploads/2023/02/icon-don-hang-xuong.png?fbclid=IwAR17TQTFRYvSPFpIm-snXaiDDSdC5ByxHIIvvOgXOwi0O8wcxSViPUzht2Q');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanphammoi`
--

CREATE TABLE `sanphammoi` (
  `id` int(11) NOT NULL,
  `tensp` varchar(250) NOT NULL,
  `gia` varchar(100) NOT NULL,
  `hinhanh` text NOT NULL,
  `mota` text NOT NULL,
  `loai` int(2) NOT NULL,
  `sltonkho` int(3) NOT NULL DEFAULT 10,
  `islivesp` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanphammoi`
--

INSERT INTO `sanphammoi` (`id`, `tensp`, `gia`, `hinhanh`, `mota`, `loai`, `sltonkho`, `islivesp`) VALUES
(70, 'LookUp Umamusume Pretty Derby Special Week', '1380000', 'https://product.hstatic.net/1000160337/product/lookup_umamusume_pretty_derby_special_week__5__530ab85a6c304a31b3235dade5223f91_master.jpg', '\"LookUp Umamusume Pretty Derby Special Week\r\n\r\nNhân vật: Special Week\r\n\r\nSeries: Umamusume Pretty Derby\r\n\r\nHãng sản xuất: MegaHouse\r\n\r\nKích thước: 11cm\"\r\n', 1, 12, 0),
(71, 'VTuber LuLu\r\n', '1850000', 'https://product.hstatic.net/1000160337/product/vtuber_lulu__1__c251fc550ec4434490407bba0529df2e_master.jpg', '\"VTuber LuLu\r\n\r\nNhân vật: LuLu\r\n\r\nSeries: [Virtual YouTuber]\r\n\r\nHãng sản xuất: QINGCANG\r\n\r\nKích thước: 9.5cm\"\r\n', 1, 12, 0),
(72, 'HELLO! GOOD SMILE Chainsaw Man Power\r\n', '720000', 'https://product.hstatic.net/1000160337/product/hello__good_smile_chainsaw_man_power__1__5df162e327d0441baa10e5afc4b27172_master.jpg', '\"HELLO! GOOD SMILE Chainsaw Man Power\r\n\r\nNhân vật: Power\r\n\r\nSeries: Chainsaw Man\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 10cm\"\r\n', 1, 12, 0),
(73, 'Nendoroid TV Anime \"JoJo\'s Bizarre Adventure\" Caesar Anthonio Zeppeli\r\n', '1850000', 'https://product.hstatic.net/1000160337/product/endoroid_tv_anime_jojo_s_bizarre_adventure_caesar_anthonio_zeppeli__1__36e9b180c1774e0ead1294cdcce4286b_master.jpg', '\"Nendoroid TV Anime \"\"JoJo\'s Bizarre Adventure\"\" Caesar Anthonio Zeppeli\r\n\r\nNhân vật: Caesar Anthonio Zeppeli\r\n\r\nSeries: JoJo\'s Bizarre Adventure Part.II Battle Tendency\r\n\r\nHãng sản xuất: Medicos Entertainment\r\n\r\nKích thước: 10cm\"\r\n', 1, 12, 0),
(74, 'Nendoroid Doll Spy x Family Yor Forger: Casual Outfit Dress Ver.\r\n', '2250000', 'https://product.hstatic.net/1000160337/product/nendoroid_doll_spy_x_family_yor_forger_casual_outfit_dress_ver__1__e935235872e0428cafb2576a659f7cff_master.jpg', '\"Nendoroid Doll Spy x Family Yor Forger: Casual Outfit Dress Ver.\r\n\r\nNhân vật: Yor Forger\r\n\r\nSeries: Spy x Family\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 14cm\"\r\n', 1, 10, 0),
(75, 'Nendoroid Hoshikuzu Telepath Umika Konohoshi\r\n', '1700000', 'https://product.hstatic.net/1000160337/product/nendoroid_hoshikuzu_telepath_umika_konohoshi__1__f767a02b24e241ca90a6a6a5a79cbecd_master.jpg', '\"Nendoroid Hoshikuzu Telepath Umika Konohoshi\r\n\r\nNhân vật: Umika Konohoshi\r\n\r\nSeries: Hoshikuzu Telepath (Stardust Telepath)\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 10cm\"\r\n', 1, 10, 0),
(76, 'Nendoroid Rebuild of Evangelion Misato Katsuragi\r\n', '1880000', 'https://product.hstatic.net/1000160337/product/nendoroid_mochiyama_kingyo_mamehinata__1__2c1b190f91904fe1a03dc3778c955a6d_master.jpg', '\"Nendoroid Rebuild of Evangelion Misato Katsuragi\r\n\r\nNhân vật: Misato Katsuragi\r\n\r\nSeries: Rebuild of Evangelion\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 10cm\"\r\n', 1, 10, 0),
(77, 'Nendoroid The Vampire Dies in No Time Ronaldo & Mebiyatsu\r\n', '1950000', 'https://product.hstatic.net/1000160337/product/nendoroid_the_vampire_dies_in_no_time_ronaldo___mebiyatsu__1__049f6490a23d488b803febf4918016ef_master.jpg', '\"Nendoroid The Vampire Dies in No Time Ronaldo & Mebiyatsu\r\n\r\nNhân vật: Ronaldo, Mebiyatsu(The Thing That Shoots Beams from its Eye)\r\n\r\nSeries: The Vampire Dies in No Time (Kyuuketsuki Sugu Shinu)\r\n\r\nHãng sản xuất: Good Smile Company, Orange Rouge\r\n\r\nKích thước: 10cm\"\r\n', 1, 10, 0),
(78, 'Nendoroid Doll Su Huan-Jen: Contest of the Endless Battle Ver.\r\n', '2680000', 'https://product.hstatic.net/1000160337/product/nendoroid_doll_su_huan-jen_contest_of_the_endless_battle_ver__4__02e5053972824133b3e88f76d297990f_master.jpg', '\"Nendoroid Doll Su Huan-Jen: Contest of the Endless Battle Ver.\r\n\r\nNhân vật: Su Huan-Jen\r\n\r\nSeries: Pili Xia Ying: Unite Against the Darkness\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 14cm\"\r\n', 1, 10, 0),
(79, 'Nendoroid Angel Beats! Kanade Tachibana\r\n', '1880000', 'https://product.hstatic.net/1000160337/product/nendoroid_angel_beats__kanade_tachibana__4__26090b62e2d64103b085685d0c2113dd_master.jpg', '\"Nendoroid Angel Beats! Kanade Tachibana\r\n\r\nNhân vật: Kanade Tachibana\r\n\r\nSeries: Angel Beats!!\r\n\r\nHãng sản xuất: Good Smile Arts Shanghai\r\n\r\nKích thước: 10cm\"\r\n', 2, 10, 0),
(80, 'POP UP PARADE Danganronpa 1.2 Reload Hajime Hinata\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_danganronpa_1.2_reload_hajime_hinata__5__9fb44db768c0489790d78939c0a18f61_master.jpg', '\"POP UP PARADE Danganronpa 1.2 Reload Hajime Hinata\r\n\r\nNhân vật: Hajime Hinata\r\n\r\nSeries: Danganronpa\r\n\r\nHãng sản xuất: Phat Company\r\n\r\nKích thước: 17cm\"\r\n', 2, 10, 0),
(81, 'POP UP PARADE The Seven Deadly Sins: Dragon\'s Judgement Gowther\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_the_seven_deadly_sins_dragon_s_judgement_gowther__4__c827458084af4d51a35e8c503dabe5ee_master.jpg', '\"POP UP PARADE The Seven Deadly Sins: Dragon\'s Judgement Gowther\r\n\r\nNhân vật: Gowther\r\n\r\nSeries: The Seven Deadly Sins (Nanatsu no Taizai)\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 17cm\"\r\n', 2, 10, 0),
(82, 'POP UP PARADE Ayakashi Triangle Suzu Kanade\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_ayakashi_triangle_suzu_kanade__6__2869707e9a6248c682d2a297c38163ee_master.jpg', '\"POP UP PARADE Ayakashi Triangle Suzu Kanade\n\nNhân vật: Suzu Kanade\n\nSeries: Ayakashi Triangle\n\nHãng sản xuất: Good Smile Company\n\nKích thước: 16.5cm\"', 2, 10, 0),
(83, 'POP UP PARADE Ayakashi Triangle Matsuri Kazamaki\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_ayakashi_triangle_matsuri_kazamaki__3__40edbb4e277742d1ac329b5ce367b1d8_master.jpg', '\"POP UP PARADE Ayakashi Triangle Matsuri Kazamaki\r\n\r\nNhân vật: Matsuri Kazamaki\r\n\r\nSeries: Ayakashi Triangle\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 18cm\"\r\n', 2, 10, 0),
(84, 'POP UP PARADE Attack on Titan Eren Yeager: Attack Titan Worldwide After Party Ver.\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/e_attack_on_titan_eren_yeager_attack_titan_worldwide_after_party_ver.2_734345c82c574b05898f89b3ea9c9c55_master.jpg', '\"POP UP PARADE Attack on Titan Eren Yeager: Attack Titan Worldwide After Party Ver.\r\n\r\nNhân vật : Eren Yeager\r\n\r\nSeries : Attack on Titan (Shingeki no Kyojin)\r\n\r\nPhiên bản : Worldwide After Party Ver.\r\n\r\nHãng sản xuất : Good Smile Company\r\n\r\nKích thước : 15cm\"', 2, 10, 0),
(85, 'POP UP PARADE Attack on Titan Reiner Braun: Armored Titan Worldwide After Party Ver.\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/ack_on_titan_reiner_braun_armored_titan_worldwide_after_party_ver.__3__d6d464f0902b40ddb3e2b4d108ee8695_master.jpg', '\"POP UP PARADE Attack on Titan Reiner Braun: Armored Titan Worldwide After Party Ver.\r\n\r\nNhân vật: Reiner Braun\r\n\r\nSeries: Attack on Titan (Shingeki no Kyojin)\r\n\r\nPhiên bản: Worldwide After Party Ver.\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 16cm\"\r\n', 2, 10, 0),
(86, 'POP UP PARADE Doki Doki Literature Club! Sayori\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_doki_doki_literature_club__sayori__6__e1784d1a349e46809c60bc4f5df3826c_master.jpg', '\"POP UP PARADE Doki Doki Literature Club! Sayori\r\n\r\nNhân vật: Sayori\r\n\r\nSeries: Doki Doki Literature Club!\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 18cm\"\r\n', 2, 10, 0),
(87, 'POP UP PARADE Overwatch 2 Tracer\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_overwatch_2_tracer___3__0fb850efdcd74d8892e4d7a1bb5fa59b_master.jpg', '\"POP UP PARADE Overwatch 2 Tracer \r\n\r\nNhân vật: Tracer \r\n\r\nSeries: Overwatch 2\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 17cm\"\r\n', 2, 10, 0),
(88, 'POP UP PARADE Hunter x Hunter Netero\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_hunter_x_hunter_netero__4__d54498a993af4a6c9f3d3bb49eda9ba0_master.jpg', '\"POP UP PARADE Hunter x Hunter Netero\r\n\r\nNhân vật: Isaac Netero\r\n\r\nSeries: Hunter x Hunter\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 17cm\"\r\n', 2, 10, 0),
(89, 'POP UP PARADE Hunter x Hunter Neferpitou\r\n', '1600000', 'https://product.hstatic.net/1000160337/product/pop_up_parade_hunter_x_hunter_neferpitou__6__cdb494d156884416bf917df5a0f61cda_master.jpg', '\"POP UP PARADE Hunter x Hunter Neferpitou\r\n\r\nNhân vật: Neferpitou\r\n\r\nSeries: Hunter x Hunter\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 16cm\"\r\n', 2, 10, 0),
(90, 'Rosado Project RS-01 Rasetsu Sekiko 1/10', '2450000', 'https://product.hstatic.net/1000160337/product/rosado_project_rs-01_rasetsu_sekiko_1__10__10125a78e9f44cc09833027501170441_master.jpg', '\"rosado Project RS-01 Rasetsu Sekiko 1/10 \r\n\r\nNhân vật: Rasetsu Sekiko\r\n\r\nSeries: Project RS-01\r\n\r\nTỷ lệ: 1/10\r\n\r\nHãng sản xuất: Earnestcore Craft\r\n\r\nKích thước: 18cm\"\r\n', 3, 10, 0),
(91, 'figma Hololive Production La+ Darknesss\r\n', '2850000', 'https://product.hstatic.net/1000160337/product/figma_hololive_production_la__darknesss__1__1471f5443d8d48b39587beeb4aeb4830_master.jpg', '\"figma Hololive Production La+ Darknesss\r\n\r\nNhân vật: La+ Darknesss\r\n\r\nSeries: Hololive, [Virtual YouTuber]\r\n\r\nHãng sản xuất: Max Factory\r\n\r\nKích thước: 13.5cm\"\r\n', 3, 10, 0),
(92, 'DFORM+ Classroom of the Elite Kei Karuizawa Deforme Action Figure\r\n', '1100000', 'https://product.hstatic.net/1000160337/product/dform__classroom_of_the_elite_kei_karuizawa_deforme_action_figure__2__234c6fe55c2d457481ce45028e8cc8e5_master.jpg', '\"DFORM+ Classroom of the Elite Kei Karuizawa Deforme Action Figure\r\n\r\nNhân vật: Kei Karuizawa\r\n\r\nSeries: Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e (Classroom of the Elite)\r\n\r\nHãng sản xuất: Good Smile Company\r\n\r\nKích thước: 9cm\"\r\n', 3, 10, 0),
(93, 'Anime \"The Master of Diabolism\" Lan Wangji 2.0 78cm Ball Joint Doll (BJD) + Teen Uniform Set\r\n', '39000000', 'https://product.hstatic.net/1000160337/product/olism_lan_wangji_2.0_78cm_ball_joint_doll__bjd____teen_uniform_set__9__a10821eda81f4b02ba1c97b9dd603964_master.jpg', '\"Anime \"\"The Master of Diabolism\"\" Lan Wangji 2.0 78cm Ball Joint Doll (BJD) + Teen Uniform Set\r\n\r\nNhân vật: Lan Wangji(Lan Zhan)\r\n\r\nSeries: The Master of Diabolism\r\n\r\nHãng sản xuất: RingDoll\r\n\r\nKích thước: 78cm\"\r\n', 3, 10, 0),
(94, 'figma Chainsaw Man Makima\r\n', '2700000', 'https://product.hstatic.net/1000160337/product/figma_chainsaw_man_makima__7__6b0fe491ef294cf68eb0183e01d7692a_master.jpg', '\"figma Chainsaw Man Makima\r\n\r\nNhân vật: Makima\r\n\r\nSeries: Chainsaw Man\r\n\r\nHãng sản xuất: Max Factory\r\n\r\nKích thước: 15cm\"\r\n', 3, 10, 0),
(95, 'Megami Device BUSTER DOLL TANK 1/1 Plastic Model\r\n', '2150000', 'https://product.hstatic.net/1000160337/product/megami_device_buster_doll_tank_11_plastic_model__2__ce5b9db2a08f4a718cb52c3ade3c0ebf_master.jpg', '\"Megami Device BUSTER DOLL TANK 1/1 Plastic Model\r\n\r\nNhân vật: BUSTER DOLL TANK\r\n\r\nSeries: Megami Device\r\n\r\nTỷ lệ: 1/1\r\n\r\nHãng sản xuất: Kotobukiya\r\n\r\nKích thước: 17.2cm\"\r\n', 3, 10, 0),
(96, 'Mega Man Battle Network Roll .EXE Plastic Model', '1980000', 'https://product.hstatic.net/1000160337/product/mega_man_battle_network_roll_.exe_plastic_model__1__150410d92b0c4a82a542271a84b5e64e_master.jpg', '\"Mega Man Battle Network Roll .EXE Plastic Model\r\n\r\nNhân vật: Roll\r\n\r\nSeries: Mega Man Battle Network (Rockman.EXE)\r\n\r\nHãng sản xuất: Kotobukiya\r\n\r\nKích thước: 13.4cm\"\r\n', 3, 10, 0),
(97, 'S.H.Figuarts Mina Ashiro \"Kaiju No. 8\"\r\n', '2200000', 'https://product.hstatic.net/1000160337/product/s.h.figuarts_mina_ashiro_kaiju_no._8__1__c271e67ee93e452f879b860cb5342541_master.jpg', '\"S.H.Figuarts Mina Ashiro \"\"Kaiju No. 8\"\"\r\n\r\nNhân vật: Mina Ashiro\r\n\r\nSeries: Kaiju No. 8 (Kaijuu 8gou)\r\n\r\nHãng sản xuất: Bandai Spirits\r\n\r\nKích thước: 14cm\"\r\n', 3, 10, 0),
(98, 'VARIABLE FIGHTER GIRLS MACROSS DELTA WALKURE MAKINA NAKAJIMA Plastic Model', '2850000', 'https://product.hstatic.net/1000160337/product/fighter_girls_macross_delta_walkure_makina_nakajima_plastic_model__10__4fd6fbfda16a403581cc210f3ac436ed_master.jpg', '\"VARIABLE FIGHTER GIRLS MACROSS DELTA WALKURE MAKINA NAKAJIMA Plastic Model\r\n\r\nNhân vật: Makina Nakajima\r\n\r\nSeries: Macross Delta\r\n\r\nHãng sản xuất: Aoshima\r\n\r\nKích thước: 16cm\"\r\n', 3, 10, 0),
(99, 'Bocchi the Rock! [BUZZmod.] GOTOH HITORI 1/12', '3000000', 'https://product.hstatic.net/1000160337/product/bocchi_the_rock___buzzmod.__gotoh_hitori_1__2__7f56971d12c44e2f89425f39af9ec2dd_master.jpg', '\"Bocchi the Rock! [BUZZmod.] GOTOH HITORI 1/12\r\n\r\nNhân vật: Hitori Goto(Bocchi)\r\n\r\nSeries: BOCCHI THE ROCK!\r\n\r\nTỷ lệ: 1/12\r\n\r\nHãng sản xuất: Aniplex\r\n\r\nKích thước: 13.5cm\"\r\n', 3, 10, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(250) NOT NULL,
  `pass` varchar(250) NOT NULL,
  `username` varchar(250) NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `uid` text NOT NULL,
  `token` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT 0,
  `islive` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `email`, `pass`, `username`, `mobile`, `uid`, `token`, `status`, `islive`) VALUES
(7, 'ntrungnghia1223@gmail.com', '12345678', 'Admin', '093123123', 'kLnGu7OjsHS2PcSXdKyz8S7PeLs2', 'dZRSC2MpQ3eYT-fqRz0z26:APA91bHQ2taGa8TxUd5T620q4XdWGbIKh7vTQ_MH1KoUhKdiCnLBT98lk2dWNPJr3Gr_5ViGjRTacP8SE9DTOW87WcJaOBIjyB_vSQhcpbI0FMMtyXl83053KJoyoBFbPWBA8o1QJCAf', 1, 0),
(30, 'appfigurenvt@gmail.com', '12345678', 'Nguyen Trung Nghia', '0385637299', 'E9Qud8ixLlcKMnKSv08feG9BGiq2', 'ch30UZ0hRdKNHPQGoLCCUT:APA91bEXlQKOKJKx5CeTYOHsZTg6Tj42XUsoOoo6eGo5WmRKfCNnHm5nYp9EzOrcsepcXR_AbDSs_FDcjppgtmU1Z6LI6K5LsLs9dRwqzCUYASK4yUmYTaF5jsXMKDv0kcc2y7YkX47A', 0, 0);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `iddonhang` (`iddonhang`,`idsp`),
  ADD KEY `chitietdonhang_ibfk_2` (`idsp`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `sanphammoi`
--
ALTER TABLE `sanphammoi`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `sanphammoi`
--
ALTER TABLE `sanphammoi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`iddonhang`) REFERENCES `donhang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`idsp`) REFERENCES `sanphammoi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
