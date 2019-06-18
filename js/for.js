        for (let j = 0; j < this.multipleSelectionAdd.length; j++) {
          for (let i = 0; i < this.orderAddDialogDetailData.poItems.length; i++) {
            if (this.multipleSelectionAdd[j].aid == this.orderAddDialogDetailData.poItems[i].aid) {
              this.orderAddDialogDetailData.poItems.splice(i, 1);
              break;
            }
          }
        }
        for (let i = 0; i < this.orderAddDialogDetailData.poItems.length; i++) {
          this.orderAddDialogDetailData.poItems[i].aid = i;
        }