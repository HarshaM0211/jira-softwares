package com.mandark.jira.web.rest;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mandark.jira.app.dto.AttachmentDTO;
import com.mandark.jira.app.service.AttachmentService;
import com.mandark.jira.spi.web.Responses;


@RestController
@RequestMapping(value = "/api/v1/issues/{issueId}/attachments")
public class AttachmentAPI extends AbstractAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentAPI.class);

    private AttachmentService attachmentService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> attach(@PathVariable("issueId") int issueId, @RequestParam String description,
            @RequestPart MultipartFile file) {
        LOGGER.info("Hit API");

        final int id = attachmentService.attach(issueId, description, file);

        final String msg =
                String.format("Successfully attached an attachment with Id : %s, to the issue of Id : %s", id, issueId);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestBody Map<String, int[]> attachmentIds) {

        final int[] ids = attachmentIds.get("attachmentIds");
        final List<Integer> attachmentsList = Arrays.stream(ids).boxed().collect(Collectors.toList());

        final String msg = attachmentService.delete(attachmentsList);
        LOGGER.info(msg);

        return Responses.ok(msg);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("issueId") int issueId, @RequestParam String cacheControlValue,
            @RequestParam String fileName) {

        final AttachmentDTO attachmentDto = attachmentService.get(issueId, fileName);
        final byte[] image = attachmentDto.getFileData();

        return Responses.imageFile(image, fileName, cacheControlValue);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

}
