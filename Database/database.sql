-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 24, 2023 at 12:14 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

create database computershop;
use computershop;

--
-- Database: `computershop`
--

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_hoa_don`
--

CREATE TABLE `chi_tiet_hoa_don` (
  `ma_hd` varchar(10) NOT NULL,
  `ma_sp` varchar(10) NOT NULL,
  `so_luong` int NOT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

CREATE TABLE `chi_tiet_phieu_nhap` (
  `ma_pn` varchar(10) NOT NULL,
  `ma_sp` varchar(10) NOT NULL,
  `so_luong` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `chi_tiet_phieu_xuat`
--

CREATE TABLE `chi_tiet_phieu_xuat` (
  `ma_px` varchar(10) NOT NULL,
  `ma_sp` varchar(10) NOT NULL,
  `so_luong` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `hang_san_pham`
--

CREATE TABLE `hang_san_pham` (
  `ma_hsp` varchar(10) NOT NULL,
  `ten_hsp` varchar(50) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hang_san_pham`
--

INSERT INTO `hang_san_pham` (`ma_hsp`, `ten_hsp`, `trang_thai`) VALUES
('HSP01', 'Dell', 0),
('HSP02', 'HP', 0),
('HSP03', 'Acer', 0),
('HSP04', 'Asus', 0),
('HSP05', 'MSI', 0),
('HSP06', 'Apple', 0),
('HSP07', 'Lenovo', 0),
('HSP08', 'Razer', 0);

-- --------------------------------------------------------

--
-- Table structure for table `hoa_don`
--

CREATE TABLE `hoa_don` (
  `ma_hd` varchar(10) NOT NULL,
  `ma_kh` varchar(10),
  `ma_nv` varchar(10) NOT NULL,
  `ngay_lap` date NOT NULL,
  `tong_tien` int NOT NULL,
  `giam_gia` int,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `khach_hang`
--

CREATE TABLE `khach_hang` (
  `ma_kh` varchar(10) NOT NULL,
  `ho_ten` varchar(50) NOT NULL,
  `dia_chi` varchar(50) NOT NULL,
  `so_dien_thoai` varchar(12) NOT NULL,
  `ngay_sinh` date NOT NULL,
  `gioi_tinh` varchar(10) NOT NULL,
  `diem_tich_luy` int NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `khach_hang`
--

INSERT INTO `khach_hang` (`ma_kh`, `ho_ten`, `dia_chi`, `so_dien_thoai`, `ngay_sinh`, `gioi_tinh`, `diem_tich_luy`, `trang_thai`) VALUES
('KH01', 'Trần Quốc Tuấn', '29 Phạm Ngọc Thạch ', '0978572812', '2000-07-03', 'Nam', 14100, 0),
('KH02', 'Nguyễn Xuân Thùy', '112 Lý Chính Thắng', '0894728493', '1999-04-09', 'Nữ', 12400, 0),
('KH03', 'Đỗ Hoàng Đạt', '956 Phạm Thế Hiển', '0983648214', '1998-02-13', 'Nam', 2300, 0);

-- --------------------------------------------------------

--
-- Table structure for table `loai_nhan_vien`
--

CREATE TABLE `loai_nhan_vien` (
  `ma_lnv` varchar(10) NOT NULL,
  `ten_lnv` varchar(50) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `loai_nhan_vien`
--

INSERT INTO `loai_nhan_vien` (`ma_lnv`, `ten_lnv`, `trang_thai`) VALUES
('LNV01', 'Admin', 0),
('LNV02', 'Quản lý', 0),
('LNV03', 'Nhân viên bán hàng', 0),
('LNV04', 'Nhân viên thủ kho', 0);

-- --------------------------------------------------------

--
-- Table structure for table `nhan_vien`
--

CREATE TABLE `nhan_vien` (
  `ma_nv` varchar(10) NOT NULL,
  `ma_lnv` varchar(10) NOT NULL,
  `ho_ten` varchar(50) NOT NULL,
  `dia_chi` varchar(50) NOT NULL,
  `so_dien_thoai` varchar(12) NOT NULL,
  `ngay_sinh` date NOT NULL,
  `gioi_tinh` varchar(10) NOT NULL,
  `email` varchar(30) NOT NULL,
  `mat_khau` varchar(12) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `nhan_vien`
--

INSERT INTO `nhan_vien` (`ma_nv`, `ma_lnv`, `ho_ten`, `dia_chi`, `so_dien_thoai`, `ngay_sinh`, `gioi_tinh`, `email`, `mat_khau`, `trang_thai`) VALUES
('NV00', 'LNV01', 'Admin', 'Admin', 'Admin', '2003-01-01', 'Nam', 'admin', 'admin', 0),
('NV01', 'LNV02', 'Nguyễn Thế Vũ', '1080 CMT8', '0975842194', '2003-01-01', 'Nam', 'thevu29@gmail.com', '123', 0),
('NV02', 'LNV03', 'Vương Huy Hoàng', '79 Trần Phú', '0974638593', '2003-09-10', 'Nam', 'huyhoang1910@gmail.com', '123', 0),
('NV03', 'LNV03', 'Trần Kim Phú', '523 Xóm Đất', '0975638294', '2003-04-15', 'Nam', 'kimphutran@gmail.com', '123', 0),
('NV04', 'LNV04', 'Huỳnh Ngọc Diễm Ly', '456 An Dương Vương', '0987563892', '2003-04-05', 'Nữ', 'diemly123@gmail.com', '123', 0);

-- --------------------------------------------------------

--
-- Table structure for table `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `ma_ncc` varchar(10) NOT NULL,
  `ho_ten` varchar(50) NOT NULL,
  `dia_chi` varchar(50) NOT NULL,
  `so_dien_thoai` varchar(12) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `nha_cung_cap`
--

INSERT INTO `nha_cung_cap` (`ma_ncc`, `ho_ten`, `dia_chi`, `so_dien_thoai`, `trang_thai`) VALUES
('NCC01', 'Công ty TNHH Thịnh Vượng', '420 Phạm Phú Thứ', '0964728237', 0),
('NCC02', 'Công ty TNHH Phát Tài', '820 CMT8', '0953678213', 0),
('NCC03', 'Thế Giới Di Động', '920 Nam Kỳ Khởi Nghĩa', '0974826312', 0),
('NCC04', 'Cellphones', '230 Bình Trị Đông', '0984753129', 0),
('NCC05', 'Phong Vũ', '651 Lạc Long Quân', '0965738245', 0),
('NCC06', 'GearVN', '213 Cao Thắng', '0974826532', 0);

-- --------------------------------------------------------

--
-- Table structure for table `phieu_nhap`
--

CREATE TABLE `phieu_nhap` (
  `ma_pn` varchar(10) NOT NULL,
  `ma_nv` varchar(10) NOT NULL,
  `ma_ncc` varchar(10) NOT NULL,
  `ngay_nhap` date NOT NULL,
  `tong_tien` int NOT NULL,
  `tinh_trang` nvarchar(20) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `phieu_xuat` (
  `ma_px` varchar(10) NOT NULL,
  `ma_nv` varchar(10) NOT NULL,
  `ngay_xuat` date NOT NULL,
  `tong_so_luong` int NOT NULL,
  `tinh_trang` nvarchar(20) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `chuong_trinh_khuyen_mai`
--

CREATE TABLE `chuong_trinh_khuyen_mai` (
  `ma_ctkm` varchar(10) NOT NULL,
  `thong_tin` nvarchar(50) NOT NULL,
  `ngay_bat_dau` date NOT NULL,
  `ngay_ket_thuc` date NOT NULL,
  `tinh_trang` nvarchar(30) NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chuong_trinh_khuyen_mai`
--

INSERT INTO `chuong_trinh_khuyen_mai` (`ma_ctkm`, `thong_tin`, `ngay_bat_dau`, `ngay_ket_thuc`, `tinh_trang`, `trang_thai`) VALUES
('CTKM01', "15%", '2023-10-01', '2023-10-31', 'Đang áp dụng', 0),
('CTKM02', "20%", '2023-11-01', '2023-11-30', 'Chưa áp dụng', 0);

--
-- Table structure for table `san_pham`
--

CREATE TABLE `san_pham` (
  `ma_sp` varchar(10) NOT NULL,
  `ma_hsp` varchar(10) NOT NULL,
  `ma_ctkm` varchar(10),
  `ten_sp` varchar(255) NOT NULL,
  `gia` int NOT NULL,
  `cpu` varchar(50) NOT NULL,
  `ram` varchar(50) NOT NULL,
  `o_cung` varchar(50) NOT NULL,
  `man_hinh` nvarchar(50) NOT NULL,
  `card_man_hinh` nvarchar(50) NOT NULL,
  `so_luong` int NOT NULL,
  `trang_thai` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `san_pham`
--

INSERT INTO `san_pham` (`ma_sp`, `ma_hsp`, `ma_ctkm`, `ten_sp`, `gia`, `cpu`, `ram`, `o_cung`, `man_hinh`, `card_man_hinh`, `so_luong`, `trang_thai`) VALUES
('SP001', 'HSP01', 'CTKM01', 'Dell Vostro 3510 R1501B', 20500000, 'Intel core I5 1135G7', '8GB', '512GB SSD', '15.6 inch FHD', '2GB MX350', 100, 0),
('SP002', 'HSP02', 'CTKM01', 'Laptop HP Pavilion 14-DV2074TU', 15790000, 'Intel core I5 1235U', '8GB', '512GB SSD', '14 inch Full HD (1920 x 1080)', 'Intel Iris Xe Graphics', 100, 0),
('SP003', 'HSP06', 'CTKM01', 'Macbook Pro 14\'\' M1 Pro 2021', 42990000, 'Apple M1 Pro', '16GB', '512GB SSD', '14.2 inch Liquid Retina XDR display (3024 x 1964)', '14 core-GPU', 100, 0),
('SP004', 'HSP03', 'CTKM01', 'Laptop Acer Nitro AN515-58-769J i7-12700H', 24190000, 'Intel core I7', '8GB', '512GB SSD', '15.6 inch Full HD (1920 x 1080)', 'NVIDIA GeForce RTX 3050', 100, 0);

--
-- Table structure for table `san_pham_kho`
--

CREATE TABLE `san_pham_kho` (
  `ma_sp` varchar(10) NOT NULL,
  `so_luong` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `san_pham`
--

INSERT INTO `san_pham_kho` (`ma_sp`, `so_luong`) VALUES
('SP001', 200),
('SP002', 250),
('SP003', 150),
('SP004', 50);

--
-- Indexes for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD PRIMARY KEY (`ma_hd`,`ma_sp`),
  ADD KEY `cthd_constraint_sp` (`ma_sp`);

--
-- Indexes for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  ADD PRIMARY KEY (`ma_pn`,`ma_sp`),
  ADD KEY `sp_constraint_pn` (`ma_sp`);

--
-- Indexes for table `chi_tiet_phieu_xuat`
--
ALTER TABLE `chi_tiet_phieu_xuat`
  ADD PRIMARY KEY (`ma_px`,`ma_sp`),
  ADD KEY `sp_constraint_px` (`ma_sp`);

--
-- Indexes for table `hang_san_pham`
--
ALTER TABLE `hang_san_pham`
  ADD PRIMARY KEY (`ma_hsp`);

--
-- Indexes for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD PRIMARY KEY (`ma_hd`),
  ADD KEY `hd_constraint_kh` (`ma_kh`),
  ADD KEY `hd_constraint_nv` (`ma_nv`);

--
-- Indexes for table `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`ma_kh`);

--
-- Indexes for table `loai_nhan_vien`
--
ALTER TABLE `loai_nhan_vien`
  ADD PRIMARY KEY (`ma_lnv`);

--
-- Indexes for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD PRIMARY KEY (`ma_nv`),
  ADD KEY `loainv_constraint_nv` (`ma_lnv`);

--
-- Indexes for table `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`ma_ncc`);

--
-- Indexes for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  ADD PRIMARY KEY (`ma_pn`),
  ADD KEY `nv_constraint_pn` (`ma_nv`),
  ADD KEY `ncc_constraint_pn` (`ma_ncc`);

--
-- Indexes for table `phieu_xuat`
--
ALTER TABLE `phieu_xuat`
  ADD PRIMARY KEY (`ma_px`),
  ADD KEY `nv_constraint_px` (`ma_nv`);

--
-- Indexes for table `chuong_trinh_khuyen_mai`
--

ALTER TABLE `chuong_trinh_khuyen_mai`
  ADD PRIMARY KEY (`ma_ctkm`);

--
-- Indexes for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD PRIMARY KEY (`ma_sp`),
  ADD KEY `hsp_constraint_sp` (`ma_hsp`),
  ADD KEY `ctkm_constraint_sp` (`ma_ctkm`);

--
-- Indexes for table `san_pham_kho`
--
ALTER TABLE `san_pham_kho`
  ADD PRIMARY KEY (`ma_sp`),
  ADD KEY `sp_kho_constraint_sp` (`ma_sp`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD CONSTRAINT `cthd_constraint_hd` FOREIGN KEY (`ma_hd`) REFERENCES `hoa_don` (`ma_hd`),
  ADD CONSTRAINT `cthd_constraint_sp` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`);

--
-- Constraints for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  ADD CONSTRAINT `ctpn_constraint_pn` FOREIGN KEY (`ma_pn`) REFERENCES `phieu_nhap` (`ma_pn`),
  ADD CONSTRAINT `sp_constraint_pn` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`);

--
-- Constraints for table `chi_tiet_phieu_xuat`
--
ALTER TABLE `chi_tiet_phieu_xuat`
  ADD CONSTRAINT `ctpx_constraint_px` FOREIGN KEY (`ma_px`) REFERENCES `phieu_xuat` (`ma_px`),
  ADD CONSTRAINT `sp_constraint_px` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`);

--
-- Constraints for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD CONSTRAINT `hd_constraint_kh` FOREIGN KEY (`ma_kh`) REFERENCES `khach_hang` (`ma_kh`),
  ADD CONSTRAINT `hd_constraint_nv` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`);

--
-- Constraints for table `nhan_vien`
--
ALTER TABLE `nhan_vien`
  ADD CONSTRAINT `loainv_constraint_nv` FOREIGN KEY (`ma_lnv`) REFERENCES `loai_nhan_vien` (`ma_lnv`);

--
-- Constraints for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  ADD CONSTRAINT `ncc_constraint_pn` FOREIGN KEY (`ma_ncc`) REFERENCES `nha_cung_cap` (`ma_ncc`),
  ADD CONSTRAINT `nv_constraint_pn` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`);

--
-- Constraints for table `phieu_xuat`
--
ALTER TABLE `phieu_xuat`
  ADD CONSTRAINT `nv_constraint_px` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`);

--
-- Constraints for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD CONSTRAINT `hsp_constraint_sp` FOREIGN KEY (`ma_hsp`) REFERENCES `hang_san_pham` (`ma_hsp`),
  ADD CONSTRAINT `ctkm_constraint_sp` FOREIGN KEY (`ma_ctkm`) REFERENCES `chuong_trinh_khuyen_mai` (`ma_ctkm`);

--
-- Constraints for table `san_pham_kho`
--
ALTER TABLE `san_pham_kho`
  ADD CONSTRAINT `sp_kho_constraint_sp` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
