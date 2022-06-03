
const allBatik = async (req, res) => {
        res.json({
        status: 'success',
        message: null,
        data: {}
        });
}


const idBatik = async (req, res) => {
    try {
      const batik = await batik.finduniqe({
          where: {
              id: req.params.id,
              body: req.body
          }
      });
      res.json(batik[0]);
    } catch (error) {
      res.json({
          message: error.message,
          data: {}
      });
    }
  }







export {allBatik, idBatik};