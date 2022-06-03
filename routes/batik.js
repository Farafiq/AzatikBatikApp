import express from 'express';
import { allBatik, idBatik, } from '../handlers/batik.js';

const router = express.Router();

//tampilan awal list batik 
router.get('/', allBatik);
//tampilan batik untuk masing2 id
router.get('/:id', idBatik);


export default router;