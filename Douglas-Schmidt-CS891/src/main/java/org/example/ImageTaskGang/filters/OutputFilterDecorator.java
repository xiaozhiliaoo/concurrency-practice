package org.example.ImageTaskGang.filters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.example.ImageTaskGang.utils.Image;
import org.example.ImageTaskGang.utils.Options;

/**
 * A Decorator whose inherited applyFilter() template method calls the
 * filter() method on the Filter object passed to its constructor and
 * whose decorate() hook method then writes the results of the
 * filtered image to an output file.  Plays the role of the "Concrete
 * Decorator" in the Decorator pattern and the role of the "Concrete
 * Class" in the Template Method pattern.
 */
public class OutputFilterDecorator
        extends FilterDecorator {
    /**
     * Constructor passes the @a filter parameter up to the superclass
     * constructor, which stores it in a data member for subsequent
     * use in applyFilter(), which is both a hook method and a
     * template method.
     */
    public OutputFilterDecorator(Filter filter) {
        super(filter);
    }

    /**
     * This hook method is called with the @a image parameter after it
     * has been filtered with mFilter in the inherited applyFilter()
     * method.  decorate() stores the filtered Image in a file.
     */
    @Override
    protected Image decorate(Image image) {
        // Store the filtered image as its filename (which is derived
        // from its URL), within the appropriate filter directory to
        // organize the filtered results and write the image to the
        // file in the appropriate directory.

        // Ensure that the path exists.
        File externalFile = new File(Options.instance().getDirectoryPath(),
                this.getName());
        externalFile.mkdirs();

        // Get a reference to the file in which the image will be stored
        File imageFile = new File(externalFile, image.getFileName());

        // Store the image using try-with-resources
        try (FileOutputStream outputFile =
                     new FileOutputStream(imageFile)) {
            BufferedImage bi = image.getImage();
            ImageIO.write(bi,
                    "png",
                    outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }
}
