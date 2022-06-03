import express from 'express';
import multer from 'multer';
import { storageBatik, uploadBatik, deleteBatik } from '../handlers/scan.js';

const router = express.Router();


const fileStorage = multer.diskStorage({
    //penyimpanan foto yang di upload
    destination: (req, file, cb) => {
      cb(null, "./images");
    },
    filename: (req, file, cb) => {
      cb(null, Date.now() + "--" + file.originalname);
    },
  });
  
const upload = multer({storage: fileStorage})


router.get('/', storageBatik);
router.get('/:id', storageBatik);
router.post('/upload', upload.single("images"), uploadBatik);
router.delete("/:id", deleteBatik); //delete foto yang sudah di dapat



export default router;